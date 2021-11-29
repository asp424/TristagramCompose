package com.asp424.tristagramcompose.ui.screens.main_list

import android.annotation.SuppressLint
import android.view.WindowManager
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import com.asp424.tristagramcompose.MainActivity
import com.asp424.tristagramcompose.data.repos.main_list.MainListResponse
import com.asp424.tristagramcompose.navigator.MainScreenAct
import com.asp424.tristagramcompose.ui.screens.cells.BaseScreen
import com.asp424.tristagramcompose.ui.screens.cells.ChangePhoto
import com.asp424.tristagramcompose.ui.screens.main_list.cells.CellCard
import com.asp424.tristagramcompose.ui.screens.main_list.cells.NameAndStatusBlockMainList
import com.asp424.tristagramcompose.ui.theme.MyTheme
import com.asp424.tristagramcompose.viewmodels.MainScreenViewModel
import com.asp424.tristagramcompose.viewmodels.SingleChatViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi


@OptIn(ExperimentalAnimationApi::class)
@ExperimentalCoroutinesApi
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun MainList(
    singleChatViewModel: SingleChatViewModel,
    mainScreenViewModel: MainScreenViewModel
) {
    val users by remember(mainScreenViewModel) {
        mainScreenViewModel.users
    }.collectAsState(initial = MainListResponse.Loading)
    when (users) {
        is MainListResponse.OnSuccess -> {
            MyTheme {
                BaseScreen(
                    headerText = "Tristagram",
                    inColumn = {
                        (users as MainListResponse.OnSuccess).mainList.forEach {
                            it.apply {
                                CellCard(onClick = {
                                    singleChatViewModel.goToSingleChat(
                                        id, fullName!!, MainScreenAct
                                    ) {}
                                }) {
                                    ChangePhoto(
                                        fullName = fullName!!,
                                        photoUrl = photoUrl!!,
                                        color = color!!,
                                        onClick = {}, id = id) {
                                        NameAndStatusBlockMainList(
                                            lastMessage = lastMessage!!,
                                            lastMessageTime = lastMessageTime!!,
                                            wasReading = was_reading!!,
                                            fullName = fullName!!,
                                            from = from!!
                                        )
                                    }
                                }
                            }
                        }
                    },
                    onBackPressed = {}, inRow = {}, verticalArrangement = Arrangement.Top,
                    onSettingIconPressed = { mainScreenViewModel.goToSettings() },
                    enableSettings = true, enableBack = false, onBackPressedEnable = false
                    , userName = { _, _, _ -> })
            }
        }
        is MainListResponse.Loading -> {
        }
        is MainListResponse.Empty -> {
        }
        else -> {
        }
    }
    LocalLifecycleOwner.current.lifecycle.addObserver(mainScreenViewModel)
}







