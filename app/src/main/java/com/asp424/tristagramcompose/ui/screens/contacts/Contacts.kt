package com.asp424.tristagramcompose.ui.screens.contacts

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel

import com.asp424.tristagramcompose.MainActivity
import com.asp424.tristagramcompose.data.firebase.models.UserModel
import com.asp424.tristagramcompose.data.repos.contacts.ContactsResponse
import com.asp424.tristagramcompose.navigator.ContactsAct
import com.asp424.tristagramcompose.ui.screens.cells.BaseScreen
import com.asp424.tristagramcompose.ui.screens.cells.ChangePhoto
import com.asp424.tristagramcompose.ui.screens.contacts.cells.NameAndStatusBlockContacts
import com.asp424.tristagramcompose.ui.screens.main_list.cells.CellCard
import com.asp424.tristagramcompose.ui.theme.MyTheme
import com.asp424.tristagramcompose.viewmodels.ContactsViewModel
import com.asp424.tristagramcompose.viewmodels.SingleChatViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@SuppressLint("UnrememberedMutableState")
@OptIn(androidx.compose.animation.ExperimentalAnimationApi::class)
@RequiresApi(Build.VERSION_CODES.N)
@ExperimentalCoroutinesApi
@Composable
fun Contacts(
    singleChatViewModel: SingleChatViewModel,
    contactsViewModel: ContactsViewModel = viewModel(
        factory = (LocalContext.current as MainActivity).viewModelFactory.get()
    )
) {
    LocalLifecycleOwner.current.lifecycle.addObserver(contactsViewModel)

    var mapUsers = hashMapOf<String, UserModel>()
    val contact by remember(contactsViewModel) { contactsViewModel.contacts }
        .collectAsState(initial = ContactsResponse.Loading)
    val kickIt by contactsViewModel.kickCompose.observeAsState(0)
    var kick by remember { mutableStateOf(0) }
    kick = kickIt
    MyTheme {
        when (contact) {
            is ContactsResponse.ChangedList -> {
                BaseScreen(
                    headerText = "Contacts",
                    verticalArrangement = Arrangement.Top,
                    inRow = {},
                    inColumn = {
                        mapUsers = (contact as ContactsResponse.ChangedList).contacts
                        mapUsers.forEach { contact ->
                            contact.value.apply {
                                CellCard(onClick = {
                                    singleChatViewModel.goToSingleChat(id, fullname, ContactsAct) {}
                                }) {
                                    ChangePhoto(fullname, photoUrl, color, inRow = {
                                        NameAndStatusBlockContacts(fullname.ifEmpty {phone}, state)
                                    }, onClick = {}, id = id)
                                }
                            }
                        }
                    },
                    onBackPressed = { contactsViewModel.goToSettings() },
                    onSettingIconPressed = {}, userName = { _, _, _ -> }
                )
            }
            is ContactsResponse.Cancelled -> {
            }
            is ContactsResponse.Loading -> {
            }
            is ContactsResponse.Empty -> {
            }
            else -> {
            }
        }
    }

}


