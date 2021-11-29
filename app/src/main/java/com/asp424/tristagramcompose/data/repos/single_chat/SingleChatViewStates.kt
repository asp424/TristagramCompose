package com.asp424.tristagramcompose.data.repos.single_chat

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.asp424.tristagramcompose.data.firebase.models.UserModel
import com.asp424.tristagramcompose.data.room.single_chat.HeaderModelRoom
import com.asp424.tristagramcompose.data.room.single_chat.MessageModelRoom

sealed class MessageResponse {
    data class OnSuccess(val messages: List<MessageModelRoom>) : MessageResponse()
    data class OnSuccessLastMessage(val message: MessageModelRoom) : MessageResponse()
    data class OnError(val error: String) : MessageResponse()
    object Loading : MessageResponse()
    object Empty : MessageResponse()
    val result: Any
        get() = when (this) {
            is OnSuccess -> {
                messages
            }
            is OnSuccessLastMessage -> {
                message
            }
            is OnError -> {
                error
            }
            else -> {}
        }
}

sealed class UserInfoResponse {
    data class OnSuccess(val userInfo: HeaderModelRoom) : UserInfoResponse()
    data class OnError(val error: String) : UserInfoResponse()
    object Loading : UserInfoResponse()
    object Empty : UserInfoResponse()
    val result: Any
        get() = when (this) {
            is OnSuccess -> {
                userInfo
            }
            is OnError -> {
                error
            }
            else -> {}
        }
}