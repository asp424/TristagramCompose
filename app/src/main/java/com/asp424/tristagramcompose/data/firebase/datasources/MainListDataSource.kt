package com.asp424.tristagramcompose.data.firebase.datasources

import android.util.Log
import com.asp424.tristagramcompose.data.firebase.models.MainListModel
import com.asp424.tristagramcompose.data.firebase.models.PhoneContactModel
import com.asp424.tristagramcompose.data.firebase.models.UserModel
import com.asp424.tristagramcompose.data.firebase.utils.getMainListModel
import com.asp424.tristagramcompose.data.firebase.utils.getMainModelRoom
import com.asp424.tristagramcompose.data.firebase.utils.getMessageModelRoom
import com.asp424.tristagramcompose.data.firebase.utils.getPhoneContactModel
import com.asp424.tristagramcompose.data.repos.contacts.ContactsResponse
import com.asp424.tristagramcompose.data.repos.main_list.MainListInterface
import com.asp424.tristagramcompose.data.repos.main_list.MainListResponse
import com.asp424.tristagramcompose.data.room.main_list.MainModelRoom
import com.asp424.tristagramcompose.utils.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MainListDataSource @Inject constructor() : MainListInterface {
    private val base = FirebaseDatabase.getInstance().reference
    override suspend fun getCurrentUserModel(result: (Int) -> Unit) {
        FirebaseDatabase.getInstance().reference.child(NODE_USERS)
            .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    USER = snapshot.getValue(UserModel::class.java) ?: UserModel()
                    if (USER.username.isEmpty()) {
                        USER.username = FirebaseAuth.getInstance().currentUser?.uid.toString()
                    }
                    if (USER.fullname.isEmpty())
                        USER.fullname = USER.phone
                    result(1)
                }

                override fun onCancelled(error: DatabaseError) {
                    result(0)
                }
            })
    }

    override suspend fun updateContacts(
        listContacts: MutableList<PhoneContactModel>,
        result: (Int) -> Unit
    ) {
        val currentUser = FirebaseAuth.getInstance().currentUser?.uid.toString()
        when (val resultQuery = base.child(NODE_PHONES).updateContact()) {
            is ContactsResponse.ChangedUpdate -> {
                resultQuery.snapshot.children.forEach { phone ->
                    listContacts.forEach { contact ->
                        if (phone.key == contact.phone) {
                            base.child(NODE_PHONES_CONTACTS).child(currentUser)
                                .child(phone.value.toString()).child(CHILD_ID)
                                .setValue(phone.value.toString())
                                .addOnCompleteListener {
                                    base.child(NODE_PHONES_CONTACTS).child(currentUser)
                                        .child(phone.value.toString()).child(CHILD_FULLNAME)
                                        .setValue(contact.fullname)
                                        .addOnCompleteListener {
                                            base.child(NODE_PHONES_CONTACTS).child(currentUser)
                                                .child(phone.value.toString()).child(CHILD_PHONE)
                                                .setValue(phone.key)
                                                .addOnCompleteListener {
                                                    result(1)
                                                }
                                                .addOnSuccessListener {
                                                    result(1)
                                                }
                                                .addOnFailureListener {
                                                    result(0)
                                                }
                                                .addOnCanceledListener {
                                                    result(0)
                                                }
                                        }
                                        .addOnCanceledListener {
                                            result(0)
                                        }
                                        .addOnFailureListener {
                                            result(0)
                                        }
                                }
                        }
                    }
                }
            }
            is ContactsResponse.Cancelled -> {
                result(0)
            }
        }
    }

    private suspend fun DatabaseReference.updateContact(): ContactsResponse =
        suspendCoroutine { continuation ->
            val valueEventListener = object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    continuation.resume(ContactsResponse.Cancelled(error.message))
                }
                override fun onDataChange(snapshot: DataSnapshot) {
                    continuation.resume(ContactsResponse.ChangedUpdate(snapshot))
                }
            }
            addListenerForSingleValueEvent(valueEventListener)
        }

    override suspend fun updateStateExit(function: () -> Unit) {
        val base = FirebaseDatabase.getInstance().reference
        val currentUid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val dateMap = mutableMapOf<String, Any>()
        dateMap[CHILD_STATE] = Calendar.getInstance().time.time.toString()
        if (FirebaseAuth.getInstance().currentUser != null) {
            base.child(NODE_USERS).child(currentUid).updateChildren(dateMap)
                .addOnCompleteListener {
                    function()
                }
        }
    }

    override suspend fun updateStateOnLine() {
        val base = FirebaseDatabase.getInstance().reference
        val currentUid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val dateMap = mutableMapOf<String, Any>()
        dateMap[CHILD_STATE] = ONLINE
        if (FirebaseAuth.getInstance().currentUser != null) {
            base.child(NODE_USERS).child(currentUid).updateChildren(dateMap)
        }
    }

    override suspend fun checkForAuth(result: (Boolean) -> Unit) {
        if (FirebaseAuth.getInstance().currentUser != null) result(true) else result(false)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun getMainList(): Flow<MainListResponse> = callbackFlow {
        var refUsers: DatabaseReference?
        var userListener: ValueEventListener?
        var refLastMessage: Query?
        var lastMessageListener: ValueEventListener?
        val mapUsers = hashMapOf<String, MainModelRoom>()
        val mapLastMessage = hashMapOf<Query, ValueEventListener>()
        val mapListeners = hashMapOf<DatabaseReference, ValueEventListener>()
        val base = FirebaseDatabase.getInstance().reference
        val currentUid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val mRefMainList = base.child(NODE_MAIN_LIST).child(currentUid)
        val parentListener = getParentList { list ->
            if (list.isEmpty()) trySendBlocking(MainListResponse.Empty)
            list.forEach { inMainList ->
                when (inMainList.type) {
                    TYPE_CHAT -> {
                        refUsers = base.child(NODE_USERS).child(inMainList.id)
                        userListener = getUser { user ->
                            user.type = TYPE_CHAT
                            if (user.fullName!!.isEmpty()) {
                                base.child(NODE_PHONES_CONTACTS).child(currentUid).child(user.id)
                                    .addListenerForSingleValueEvent(getUserFullName { name ->
                                        user.fullName = name
                                        refLastMessage = base.child(NODE_MESSAGES)
                                            .child(currentUid).child(user.id).limitToLast(1)
                                        lastMessageListener =
                                            getLastMessage { lastMessage, timeStamp, was_reading, from ->
                                                if (user.state == "печатает...")
                                                    user.lastMessage = "печатает..."
                                                else user.lastMessage = lastMessage
                                                user.from = from
                                                user.was_reading = was_reading
                                                user.lastMessageTime = timeStamp
                                                mapUsers[user.id] = user
                                                if (list.size == mapUsers.size) {
                                                    trySendBlocking(
                                                        MainListResponse.OnSuccessHash(
                                                            mapUsers
                                                        )
                                                    )
                                                }
                                            }
                                        refLastMessage!!
                                            .addValueEventListener(lastMessageListener as ValueEventListener)
                                        mapLastMessage[refLastMessage!!] =
                                            lastMessageListener as ValueEventListener
                                    })
                            } else {
                                refLastMessage = base.child(NODE_MESSAGES)
                                    .child(currentUid).child(user.id).limitToLast(1)
                                lastMessageListener =
                                    getLastMessage { lastMessage, timeStamp, was_reading, from ->
                                        if (user.state == "печатает...")
                                            user.lastMessage = "печатает..."
                                        user.from = from
                                        user.was_reading = was_reading
                                        user.lastMessage = lastMessage
                                        user.lastMessageTime = timeStamp
                                        mapUsers[user.id] = user
                                        if (list.size == mapUsers.size)
                                            trySendBlocking(MainListResponse.OnSuccessHash(mapUsers))
                                    }
                                refLastMessage!!.addValueEventListener(lastMessageListener as ValueEventListener)
                                mapLastMessage[refLastMessage!!] =
                                    lastMessageListener as ValueEventListener
                            }
                        }
                        refUsers!!.addValueEventListener(userListener as ValueEventListener)
                        mapListeners[refUsers!!] = userListener as ValueEventListener
                    }
                }
            }
        }
        mRefMainList.addValueEventListener(parentListener)
        awaitClose {
            mapLastMessage.forEach {
                it.key.removeEventListener(it.value)
            }
            mapListeners.forEach {
                it.key.removeEventListener(it.value)
            }
            mRefMainList.removeEventListener(parentListener)
        }
    }

    private fun getParentList(function: (List<MainListModel>) -> Unit) =
        object : ValueEventListener {
            val list = mutableListOf<MainListModel>()
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.value == null)
                    function(list)
                snapshot.children.forEach { user ->
                    list.add(user.getMainListModel())
                    if (list.size == snapshot.childrenCount.toInt())
                        function(list)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }

    private fun getUser(function: (MainModelRoom) -> Unit) =
        object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                function(snapshot.getMainModelRoom())
            }
            override fun onCancelled(error: DatabaseError) {}
        }

    private fun getUserFullName(function: (String) -> Unit) =
        object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val contact = snapshot.getPhoneContactModel()
                if (contact.fullname.isEmpty())
                    function(contact.phone)
                else function(contact.fullname)
            }
            override fun onCancelled(error: DatabaseError) {}
        }

    private fun getLastMessage(function: (String, String, String, String) -> Unit) =
        object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.value == null)
                    function("Сообщений пока нет", "", "", "")
                snapshot.children.forEach {
                    it.getMessageModelRoom().apply {
                        function(
                            text!!,
                            timeStamp!!,
                            was_reading!!,
                            from!!
                        )
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }
}




