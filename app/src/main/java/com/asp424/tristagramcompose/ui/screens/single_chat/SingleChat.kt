package com.asp424.tristagramcompose.ui.screens.single_chat


import android.annotation.SuppressLint
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalLifecycleOwner
import com.asp424.tristagramcompose.data.repos.single_chat.MessageResponse
import com.asp424.tristagramcompose.data.room.single_chat.MessageModelRoom
import com.asp424.tristagramcompose.ui.screens.cells.BaseScreen
import com.asp424.tristagramcompose.ui.screens.single_chat.cells.LazyColumnChat
import com.asp424.tristagramcompose.ui.screens.single_chat.cells.SendBlock
import com.asp424.tristagramcompose.viewmodels.SingleChatViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi


@SuppressLint(
    "CoroutineCreationDuringComposition",
    "UnrememberedMutableState"
)
@OptIn(
    ExperimentalCoroutinesApi::class,
    ExperimentalFoundationApi::class,
    ExperimentalComposeUiApi::class,
    ExperimentalAnimationApi::class,
    ExperimentalMaterialApi::class
)

@Composable
fun SingleChat(
    singleChatViewModel: SingleChatViewModel
) {
    var heightKeyboard by remember {
        mutableStateOf(0f)
    }
    val focusRequester = remember { FocusRequester() }

    val messages by remember(singleChatViewModel) {
        singleChatViewModel.messages
    }.collectAsState()

    var userName: String? = null
    var token: String? = null

    BaseScreen(
        singleChatViewModel = singleChatViewModel,
        headerText = "",
        verticalArrangement = Arrangement.Bottom,
        inRow = {},
        inColumn = {
            when (messages) {
                is MessageResponse.OnSuccess -> {
                    val newList = mutableStateListOf<MessageModelRoom>()
                    (messages as MessageResponse.OnSuccess)
                        .messages.forEach {
                            newList.add(it)
                        }
                    LazyColumnChat(
                        list = newList, singleChatViewModel, userName, onSwipeLeft = {
                            focusRequester.requestFocus()
                        }
                    )
                }
                is MessageResponse.OnError -> {

                }
                is MessageResponse.Empty -> {

                }
                is MessageResponse.Loading -> {

                }
                else -> {
                }
            }
        },
        onBackPressed = {
            singleChatViewModel.apply {
                goBack()
            }
        },
        enableSettings = false,
        enableHeader = false,
        onSettingIconPressed = {}, userName = { userName1, token1, _ ->
            userName = userName1
            token = token1
        })

    SendBlock(
        singleChatViewModel,
        onTextFieldFocused = {

        }, focusRequester, token
    )


    LocalLifecycleOwner.current.lifecycle.addObserver(singleChatViewModel)
}
