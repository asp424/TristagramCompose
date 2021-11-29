package com.asp424.tristagramcompose.data.firebase.datasources

import com.asp424.tristagramcompose.data.repos.contacts.ContactsInterface
import com.asp424.tristagramcompose.data.repos.contacts.ContactsResponse
import com.asp424.tristagramcompose.data.firebase.models.UserModel
import com.asp424.tristagramcompose.data.firebase.utils.getPhoneContactModel
import com.asp424.tristagramcompose.data.firebase.utils.getUserModel
import com.asp424.tristagramcompose.utils.NODE_PHONES_CONTACTS
import com.asp424.tristagramcompose.utils.NODE_USERS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class ContactsDataSource @Inject constructor() : ContactsInterface {
    @ExperimentalCoroutinesApi
    override suspend fun getContacts() =
        callbackFlow {
            val mapUsers = hashMapOf<String, UserModel>()
            val mapListeners = hashMapOf<DatabaseReference, ValueEventListener>()
            val base = FirebaseDatabase.getInstance().reference
            val currentUser = FirebaseAuth.getInstance().currentUser?.uid.toString()
            var refUsers: DatabaseReference? = null
            var contactListener: ValueEventListener? = null
            val mRefContacts = base.child(NODE_PHONES_CONTACTS).child(currentUser)
            val contactsListener = parentListener { list ->
                if (list.isEmpty()) trySendBlocking(ContactsResponse.Empty)
                list.forEach { user ->
                    refUsers = base.child(NODE_USERS).child(user.value)
                    contactListener = userListener(user.key) {
                        when(it){
                            is ContactsResponse.Changed -> {
                                val result = it.contact
                                mapUsers[result.id] = result
                                if (mapUsers.size == list.size)
                                    trySendBlocking(ContactsResponse.ChangedList(mapUsers)) }
                            is ContactsResponse.Cancelled -> { trySendBlocking(it) }
                            is ContactsResponse.Loading -> { trySendBlocking(it) }
                            else -> {}
                        }
                    }
                    refUsers!!.addValueEventListener(contactListener as ValueEventListener)
                    mapListeners[refUsers!!] = contactListener as ValueEventListener
                }
            }
            mRefContacts.addValueEventListener(contactsListener)
            awaitClose {
                if (refUsers != null && contactListener != null)
                   mapListeners.forEach {
                       it.key.removeEventListener(it.value)
                   }
                mRefContacts.removeEventListener(contactsListener)
            }
        }

    private fun userListener(fullName: String, function: (ContactsResponse) -> Unit) =
        object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val model = snapshot.getUserModel()
                if (fullName.isEmpty()) model.fullname =
                    model.phone
                else model.fullname = fullName
                function(ContactsResponse.Changed(model))
            }
            override fun onCancelled(error: DatabaseError) {
                function(ContactsResponse.Cancelled(error.message))
            }
        }

    private fun parentListener(function: (HashMap<String, String>) -> Unit) =
        object : ValueEventListener {
            val list = hashMapOf<String, String>()
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.value == null)
                    function(list)
                snapshot.children.forEach { contact ->
                    list[contact.getPhoneContactModel().fullname] = contact.getPhoneContactModel().id
                    if (list.size == snapshot.childrenCount.toInt())
                    function(list)
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        }
}

