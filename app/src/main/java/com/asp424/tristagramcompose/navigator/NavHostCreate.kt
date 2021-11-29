package com.asp424.tristagramcompose.navigator

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.asp424.tristagramcompose.MainActivity
import com.asp424.tristagramcompose.application.appComponent
import com.asp424.tristagramcompose.ui.screens.account.Account
import com.asp424.tristagramcompose.ui.screens.app_settings.AppSettings
import com.asp424.tristagramcompose.ui.screens.contacts.Contacts
import com.asp424.tristagramcompose.ui.screens.main_list.MainList
import com.asp424.tristagramcompose.ui.screens.regscreen.RegScreen
import com.asp424.tristagramcompose.ui.screens.settings.Settings
import com.asp424.tristagramcompose.ui.screens.single_chat.SingleChat
import com.asp424.tristagramcompose.viewmodels.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow


@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalComposeUiApi::class, androidx.compose.animation.ExperimentalAnimationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalCoroutinesApi
@Composable
fun NavHostCreate(
    mainViewModel: MainViewModel,
    singleChatViewModel: SingleChatViewModel,
    mainScreenViewModel: MainScreenViewModel,
    registerViewModel: RegisterViewModel,
    settingsViewModel: SettingsViewModel,
    navController: NavHostController = rememberNavController(),
    ) {
  //  val currentRoute = navController
    //    .currentBackStackEntryFlow
   //     .collectAsState(initial = navController.currentBackStackEntry)
  //  Log.d("My", currentRoute.value?.destination.toString())
    val navigatorState by (LocalContext.current as MainActivity).appComponent.navigator.navActions
        .asLifecycleAwareState()
    LaunchedEffect(navigatorState) {
        navigatorState?.let {
            navController.navigate(it.destination, it.navOptions)
        }
    }
    NavHost(navController, startDestination = destination(Destinations.MainScreen)) {
        composable(route = destination(Destinations.Registration)) {
            RegScreen(LocalContext.current as MainActivity, registerViewModel)
        }
        composable(route = destination(Destinations.MainScreen)) {
            MainList(singleChatViewModel, mainScreenViewModel)
        }
        composable(route = destination(Destinations.Contacts)) {
            Contacts(singleChatViewModel)
        }
        composable(route = destination(Destinations.Settings)) {
            Settings()
        }
        composable(route = destination(Destinations.SingleChat)) {
            SingleChat(singleChatViewModel)
        }
        composable(route = destination(Destinations.Account)) {
            Account(mainScreenViewModel, mainViewModel, registerViewModel, settingsViewModel)
        }
        composable(route = destination(Destinations.AppSettings)) {
            AppSettings()
        }
    }
}


@Composable
fun <T> Flow<T>.asLifecycleAwareState() =
    lifecycleAwareState(LocalLifecycleOwner.current, this, null)

@Composable
fun <T> lifecycleAwareState(
    lifecycleOwner: LifecycleOwner,
    flow: Flow<T>,
    initialState: T
): State<T> {
    val lifecycleAwareStateFlow = remember(flow, lifecycleOwner) {
        flow.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
    }
    return lifecycleAwareStateFlow.collectAsState(initialState)
}