package com.asp424.tristagramcompose.ui.screens.account

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asp424.tristagramcompose.MainActivity
import com.asp424.tristagramcompose.navigator.RegistrationAct
import com.asp424.tristagramcompose.navigator.SettingsAct
import com.asp424.tristagramcompose.ui.screens.cells.BaseScreen
import com.asp424.tristagramcompose.ui.screens.cells.ChangePhoto
import com.asp424.tristagramcompose.ui.screens.contacts.cells.NameAndStatusBlockContacts
import com.asp424.tristagramcompose.utils.USER
import com.asp424.tristagramcompose.utils.changePhotoUser
import com.asp424.tristagramcompose.viewmodels.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.ExperimentalCoroutinesApi

@RequiresApi(Build.VERSION_CODES.N)
@OptIn(
    ExperimentalCoroutinesApi::class,
    androidx.compose.ui.ExperimentalComposeUiApi::class,
    androidx.compose.animation.ExperimentalAnimationApi::class
)
@Composable
fun Account(
    mainScreenViewModel: MainScreenViewModel,
    mainViewModel: MainViewModel,
    registrationViewModel: RegisterViewModel,
    settingsViewModel: SettingsViewModel,
    navController: NavViewModel = viewModel(
        factory = (LocalContext.current as MainActivity).viewModelFactory.get()
    )
) {
    val uri by settingsViewModel.uri.observeAsState("")
    val context = LocalContext.current as MainActivity
    BaseScreen(
        headerText = "Account",
        verticalArrangement = Arrangement.SpaceBetween,
        inRow = {},
        inColumn = {
            USER.apply {
                if (uri.isNotEmpty())
                    photoUrl = uri
                    ChangePhoto(
                        fullName = fullname.ifEmpty { phone },
                        photoUrl = photoUrl,
                        color = color,
                        onClick = {
                            changePhotoUser(context)
                        },
                        id = FirebaseAuth.getInstance().currentUser?.uid.toString()
                    ) {
                        NameAndStatusBlockContacts(
                            fullName = fullname.ifEmpty { phone },
                            state = state
                        )
                    }
            }
            Button(onClick = {
                mainScreenViewModel.deleteAllFromMainListRoom()
                mainViewModel.setExitState {
                    registrationViewModel.signOut()
                    navController.toScreen(RegistrationAct)
                }
            }, Modifier.padding(bottom = 16.dp)) {
                Text(text = "Выйти из аккаунта", fontSize = 20.sp)
            }
        },
        onBackPressed = { navController.toScreen(SettingsAct) },
        onSettingIconPressed = {}, userName = { _, _, _ -> })
}

