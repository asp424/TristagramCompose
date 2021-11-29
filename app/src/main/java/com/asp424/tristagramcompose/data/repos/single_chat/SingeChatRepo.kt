package com.asp424.tristagramcompose.data.repos.single_chat

import androidx.compose.runtime.mutableStateListOf
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.asp424.tristagramcompose.data.firebase.datasources.SingleChatDataSource
import com.asp424.tristagramcompose.data.repos.main_list.MainListRepo
import com.asp424.tristagramcompose.data.room.single_chat.HeaderDao
import com.asp424.tristagramcompose.data.room.single_chat.MessageModelRoom
import com.asp424.tristagramcompose.data.room.single_chat.SingleChatDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class SingeChatRepo @Inject constructor(
    private val singleChatDataSource: SingleChatDataSource,
    private val singleChatDao: SingleChatDao,
    private val headerDao: HeaderDao,
    mainListRepo: MainListRepo
) : SingleChatInterface {

    override suspend fun sendMessage(
        message: String,
        receivingUserID: String, typeMessage: String, token: String,
        fullName: String, photoUrl: String, function: () -> Unit
    ) {
        singleChatDataSource.sendMessage(
            message = message,
            fullName = fullName, token = token,
            typeMessage = typeMessage, receivingUserID = receivingUserID,
            photoUrl = photoUrl
        ) {
            function()
        }
    }

    override suspend fun getMessages(
        id: String,
        countMessages: Int,
        function: (MessageResponse) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {

            val list = mutableStateListOf<MessageModelRoom>()
            (singleChatDao.getAllMessages(id)).forEach {
                list.add(it)
                if (list.size == countMessages)
                    function(MessageResponse.OnSuccess(list))
            }
        }

        updateRoom(id, countMessages) {
            function(it)
        }
    }

    private suspend fun updateRoom(
        id: String,
        countMessages: Int,
        function: (MessageResponse) -> Unit
    ) {
        var count = 0
        singleChatDataSource.getMessageAss(id, countMessages).collect { response ->
            val list = mutableStateListOf<MessageModelRoom>()
            when (response) {
                is MessageResponse.OnSuccess -> {
                    response.messages.forEach { value ->
                        count++
                        singleChatDao.insertOrUpdateMessage(value)
                        if (response.messages.size == count) {
                            count = 0
                            (singleChatDao.getAllMessages(
                                id
                            ))
                                .forEach {
                                    list.add(it)
                                    if (list.size == countMessages)
                                        function(MessageResponse.OnSuccess(list))
                                }
                        }
                    }
                }
                is MessageResponse.OnError -> function(MessageResponse.OnError(response.error))
                else -> {
                }
            }
        }
    }

    suspend fun getContactForChat(
        id: String, fullName: String,
        function: (UserInfoResponse) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            headerDao.getById(id)?.let { UserInfoResponse.OnSuccess(it) }?.let { function(it) }
        }
        singleChatDataSource.getContactForChatAss(id, fullName).collect { response ->
            when (response) {
                is UserInfoResponse.OnSuccess -> {
                    function(response)
                }
                is UserInfoResponse.OnError -> function(UserInfoResponse.OnError(response.error))
                else -> {
                }
            }
            CoroutineScope(Dispatchers.IO).launch {
                headerDao.insertOrUpdateMessage((response as UserInfoResponse.OnSuccess).userInfo)
            }
        }
    }

    override suspend fun updateStateExit(function: () -> Unit) {
        singleChatDataSource.updateStateExit {
            function()
        }
    }

    override suspend fun updateStateOnLine() {
        singleChatDataSource.updateStateOnLine()
    }

    override suspend fun updateStateTyping() {
        singleChatDataSource.updateStateTyping()
    }

    override suspend fun setWasReading(id: String, messageKey: String) {
        singleChatDataSource.setWasReading(id, messageKey)
    }
}