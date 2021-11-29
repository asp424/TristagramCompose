package com.asp424.tristagramcompose.data.repos.single_chat

import kotlinx.coroutines.flow.Flow


interface SingleChatInterface {
    suspend fun sendMessage( message: String,
                             receivingUserID: String, typeMessage: String, token: String,
                             fullName: String, photoUrl: String, function: () -> Unit)
    suspend fun getMessages(id: String, countMessages: Int, function: (MessageResponse) -> Unit)
    suspend fun updateStateExit(function: ()-> Unit)
    suspend fun updateStateOnLine()
    suspend fun updateStateTyping()
    suspend fun setWasReading(id: String, messageKey: String)

}