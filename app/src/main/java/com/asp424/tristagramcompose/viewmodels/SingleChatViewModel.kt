package com.asp424.tristagramcompose.viewmodels

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.asp424.tristagramcompose.data.repos.single_chat.MessageResponse
import com.asp424.tristagramcompose.data.repos.single_chat.SingeChatRepo
import com.asp424.tristagramcompose.data.repos.single_chat.UserInfoResponse
import com.asp424.tristagramcompose.data.room.single_chat.MessageModelRoom
import com.asp424.tristagramcompose.navigator.CustomNavigator
import com.asp424.tristagramcompose.navigator.NavigationAction
import com.asp424.tristagramcompose.navigator.SingleChatAct
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SingleChatViewModel
@Inject constructor(private var repository: SingeChatRepo, private val navigator: CustomNavigator) :
    ViewModel(), DefaultLifecycleObserver {
    val nameForReply = MutableLiveData("")
    val textMessageReply = MutableLiveData("")
    val reply = MutableLiveData(false)
    val height = MutableLiveData(0f)
    val stateReplyBlock = MutableLiveData(false)
    val keyBoardState = MutableLiveData(false)

    fun setDataForReply(
        text: String?, userName: String?,
        stateReplyBlockValue: Boolean
    ) =
        viewModelScope.launch(Dispatchers.IO) {
            nameForReply.postValue(userName)
            textMessageReply.postValue(text)
            stateReplyBlock.postValue(stateReplyBlockValue)

        }

    private val destination = MutableLiveData<NavigationAction>()
    private fun setBackDest(destinationAct: NavigationAction, function: () -> Unit) {
        destination.value = destinationAct
        function()
    }

    private val contactId = MutableLiveData("")
    private val contactFullName = MutableLiveData("")

    private val _userInfo: MutableStateFlow<UserInfoResponse?> =
        MutableStateFlow(UserInfoResponse.Loading)
    val userInfo = _userInfo.asStateFlow()

    private val _messages: MutableStateFlow<MessageResponse?> =
        MutableStateFlow(MessageResponse.Loading)
    val messages = _messages.asStateFlow()

    private var jobMessages: Job? = null
    private var jobUsersInfo: Job? = null

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        getMessages { }
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        setDataForReply(
            "", "",
            stateReplyBlockValue = false
        )
        jobMessages?.cancel()
        jobUsersInfo?.cancel()
    }

    fun sendTextMessage(
        message: String,
        typeMessage: String,
        token: String,
        fullName: String,
        photoUrl: String,
        function: () -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.sendMessage(
                message = message, receivingUserID = contactId.value!!,
                typeMessage = typeMessage, token = token, photoUrl = photoUrl, fullName = fullName
            ) {
                function()
            }
        }
    }

    private fun getMessages(countMessages: Int = 10, function: () -> Unit) {
        jobMessages?.cancel()
        jobMessages = viewModelScope.launch(Dispatchers.IO) {
            repository.getMessages(contactId.value!!, countMessages = countMessages) {
                function()
                _messages.value = it
            }
        }
        jobMessages?.start()
    }

    private fun getContactForChat(id: String, fullName: String, function: () -> Unit) {
        jobUsersInfo?.cancel()
        jobUsersInfo = viewModelScope.launch {
            repository.getContactForChat(id, fullName) {
                function()
                _userInfo.value = it
            }
        }
        jobUsersInfo?.start()
    }

    fun updateStateOnLine() {
        viewModelScope.launch {
            repository.updateStateOnLine()
        }
    }

    fun updateStateTyping() {
        viewModelScope.launch {
            repository.updateStateTyping()
        }
    }

    fun goToSingleChat(
        id: String,
        fullName: String,
        navigationAction: NavigationAction,
        function: () -> Unit
    ) {
        contactId.value = id
        contactFullName.value = fullName
        setBackDest(navigationAction) {
            navigator.navigate(SingleChatAct)
            getMessages {
                getContactForChat(
                    id,
                    fullName
                ) {
                    function()
                }
            }
        }
    }

    fun goBack() {
        navigator.navigate(destination.value!!)
    }

    fun setWasReading(messageKey: String) {
        if (contactId.value!!.isNotEmpty())
            viewModelScope.launch(Dispatchers.Main) {
                repository.setWasReading(contactId.value!!, messageKey)
            }
    }
}





