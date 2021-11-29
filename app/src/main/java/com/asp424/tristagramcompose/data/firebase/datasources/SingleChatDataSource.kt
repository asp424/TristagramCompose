package com.asp424.tristagramcompose.data.firebase.datasources

import androidx.compose.runtime.mutableStateListOf
import com.asp424.tristagramcompose.data.firebase.utils.getHeaderModelRoom
import com.asp424.tristagramcompose.data.firebase.utils.getMessageModelRoom
import com.asp424.tristagramcompose.data.repos.single_chat.MessageResponse
import com.asp424.tristagramcompose.data.repos.single_chat.SingleChatInterface
import com.asp424.tristagramcompose.data.repos.single_chat.UserInfoResponse
import com.asp424.tristagramcompose.data.room.single_chat.MessageModelRoom
import com.asp424.tristagramcompose.notification.remote_message.retrofit.ApiClient
import com.asp424.tristagramcompose.notification.remote_message.retrofit.ApiService
import com.asp424.tristagramcompose.utils.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import javax.inject.Inject

class SingleChatDataSource @Inject constructor() : SingleChatInterface {

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun sendMessage(
        message: String,
        receivingUserID: String,
        typeMessage: String,
        token: String,
        fullName: String,
        photoUrl: String,
        function: () -> Unit

    ) {
        val currentUid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val base = FirebaseDatabase.getInstance().reference
        val refDialogUser = "$NODE_MESSAGES/$currentUid/$receivingUserID"
        val messageKey = base.child(refDialogUser).push().key
        val mapMessage = hashMapOf<String, Any>()
        mapMessage[CHILD_FROM] = currentUid
        mapMessage[CHILD_TEXT] = message
        mapMessage[CHILD_ID] = messageKey.toString()
        mapMessage[CHILD_TIMESTAMP] = Calendar.getInstance().time.time.toString()
        mapMessage[CHILD_KEY] = receivingUserID
        base.child(NODE_MESSAGES).child(currentUid)
            .child(receivingUserID).child(messageKey!!)
            .updateChildren(mapMessage)
            .addOnCompleteListener {
                mapMessage[CHILD_KEY] = currentUid
                base.child(NODE_MESSAGES).child(receivingUserID)
                    .child(currentUid).child(messageKey)
                    .updateChildren(mapMessage)
                    .addOnCompleteListener {
                        function()
                        sendRemoteMessage(fullName = fullName, token = token, photoUrl = photoUrl,
                        textMessage = message)
                        CoroutineScope(Dispatchers.IO).launch {
                            saveToMainList(receivingUserID, typeMessage) {
                            }
                        }
                    }
            }
    }

    override suspend fun getMessages(
        id: String,
        countMessages: Int,
        function: (MessageResponse) -> Unit
    ) {

    }

    private fun saveToMainList(id: String, type: String, function: () -> Unit) {
        val currentUid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val base = FirebaseDatabase.getInstance().reference
        val refUser = "$NODE_MAIN_LIST/$currentUid/$id"
        val refReceived = "$NODE_MAIN_LIST/$id/$currentUid"
        val mapUser = hashMapOf<String, Any>()
        val mapReceived = hashMapOf<String, Any>()
        mapUser[CHILD_ID] = id
        mapUser[CHILD_TYPE] = type
        mapReceived[CHILD_ID] = currentUid
        mapReceived[CHILD_TYPE] = type
        val commonMap = hashMapOf<String, Any>()
        commonMap[refUser] = mapUser
        commonMap[refReceived] = mapReceived
        base.updateChildren(commonMap)
            .addOnFailureListener { function() }
            .addOnCompleteListener { function() }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun getMessageAss(id: String, countMessages: Int): Flow<MessageResponse> = callbackFlow {
        val mRefMessages = FirebaseDatabase.getInstance().reference
            .child(NODE_MESSAGES)
            .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .child(id)
            //.limitToLast(countMessages)
        val listener = lastMessageListener { trySendBlocking(it) }
        mRefMessages.addValueEventListener(listener)
        awaitClose { mRefMessages.removeEventListener(listener) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun getContactForChatAss(id: String, fullName: String): Flow<UserInfoResponse> =
        callbackFlow {
            val refUsers = FirebaseDatabase.getInstance().reference.child(NODE_USERS).child(id)
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val model = snapshot.getHeaderModelRoom()
                    if (fullName.isEmpty()) model.fullname =
                        fullName
                    else model.fullname = fullName
                    kotlin.runCatching {
                        trySendBlocking(UserInfoResponse.OnSuccess(model))
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    kotlin.runCatching {
                        trySendBlocking(UserInfoResponse.OnError(error.message))
                    }
                }
            }
            refUsers.addValueEventListener(listener)
            awaitClose {
                refUsers.removeEventListener(listener)
            }
        }

    private fun lastMessageListener(function: (MessageResponse) -> Unit) =
        object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.value == null)
                    function(MessageResponse.Empty)
                val list = mutableStateListOf<MessageModelRoom>()
                snapshot.children.forEach { message ->
                    list.add(message.getMessageModelRoom())
                    if (list.size == snapshot.childrenCount.toInt())
                        function(MessageResponse.OnSuccess(list))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                function(MessageResponse.OnError(error.message))
            }
        }

    override suspend fun updateStateExit(function: () -> Unit) {
        val currentUid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val dateMap = mutableMapOf<String, Any>()
        dateMap[CHILD_STATE] = Calendar.getInstance().time.time.toString()
        if (FirebaseAuth.getInstance().currentUser != null) {
            FirebaseDatabase.getInstance().reference.child(NODE_USERS).child(currentUid)
                .updateChildren(dateMap)
                .addOnCompleteListener {
                    function()
                }
        }
    }

    override suspend fun updateStateOnLine() {
        val currentUid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val dateMap = mutableMapOf<String, Any>()
        dateMap[CHILD_STATE] = ONLINE
        if (FirebaseAuth.getInstance().currentUser != null) {
            FirebaseDatabase.getInstance().reference.child(NODE_USERS).child(currentUid)
                .updateChildren(dateMap)
        }
    }

    override suspend fun updateStateTyping() {
        val currentUid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val dateMap = mutableMapOf<String, Any>()
        dateMap[CHILD_STATE] = TYPING
        if (FirebaseAuth.getInstance().currentUser != null) {
            FirebaseDatabase.getInstance().reference.child(NODE_USERS).child(currentUid)
                .updateChildren(dateMap)
        }
    }

    override suspend fun setWasReading(id: String, messageKey: String) {
        val map = hashMapOf<String, Any>()
        map[WAS_READING] = WAS_READING
        FirebaseDatabase.getInstance().reference.child(NODE_MESSAGES).child(id)
            .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .child(messageKey).updateChildren(map)
    }
    private fun sendRemoteMessage(token: String, fullName: String, textMessage: String, photoUrl: String) {
        val headers = HashMap<String, String>()
        headers["Content-Type"] = "application/json"
        headers["Authorization"] = API_KEY
        ApiClient.client?.create(ApiService::class.java)?.sendRemoteMessage(
            headers, JSONObject()
                .put("data", JSONObject()
                    .put("fullName", fullName)
                    .put("textMessage", textMessage)
                    .put("photoUrl", photoUrl)
                    .put("userUid", FirebaseAuth.getInstance().currentUser?.uid.toString()))
                .put("registration_ids", JSONArray().put(token)).toString()
        )?.enqueue(object : Callback<String?> {
            override fun onResponse(call: Call<String?>, response: Response<String?>) {}
            override fun onFailure(call: Call<String?>, t: Throwable) {}
        })
    }
}


