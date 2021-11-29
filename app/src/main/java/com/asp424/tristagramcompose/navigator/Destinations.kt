package com.asp424.tristagramcompose.navigator

import androidx.compose.animation.fadeIn
import androidx.navigation.NavOptions

interface NavigationAction {
    val destination: String
    val navOptions: NavOptions
        get() = NavOptions.Builder().build()

}

sealed interface Destinations {
object MainScreen : Destinations
object Settings : Destinations
object Contacts : Destinations
object SingleChat : Destinations
object Account : Destinations
object Registration : Destinations
object Check : Destinations
object AppSettings : Destinations
}

object MainScreenAct : NavigationAction {
    override val destination = destination(Destinations.MainScreen)
    override val navOptions: NavOptions =  NavOptions.Builder()
        .setPopUpTo(destination(Destinations.MainScreen), true)
        .setLaunchSingleTop(true)
        .build()
}
object AppSettingsAct : NavigationAction {
    override val destination = destination(Destinations.AppSettings)
    override val navOptions: NavOptions =  NavOptions.Builder()
        .setPopUpTo(destination(Destinations.AppSettings), true)
        .setLaunchSingleTop(true)
        .build()
}

object SettingsAct : NavigationAction { override val destination = destination(Destinations.Settings)
    override val navOptions: NavOptions =  NavOptions.Builder()
        .setPopUpTo(destination(Destinations.Settings), true)
        .setLaunchSingleTop(true)
        .build()

}

object AccountAct : NavigationAction { override val destination = destination(Destinations.Account)
    override val navOptions: NavOptions =  NavOptions.Builder()
        .setPopUpTo(destination(Destinations.Account), true)
        .setLaunchSingleTop(true)
        .build()

}
object ContactsAct : NavigationAction { override val destination = destination(Destinations.Contacts)
    override val navOptions: NavOptions =  NavOptions.Builder()
        .setPopUpTo(destination(Destinations.Contacts), true)
        .setLaunchSingleTop(true)
        .build()
}
object SingleChatAct : NavigationAction { override val destination = destination(Destinations.SingleChat)
    override val navOptions: NavOptions =  NavOptions.Builder()
        .setPopUpTo(destination, true)
        .setLaunchSingleTop(true)
        .build()
}

object RegistrationAct : NavigationAction { override val destination = destination(Destinations.Registration)
    override val navOptions: NavOptions =  NavOptions.Builder()
        .setPopUpTo(destination(Destinations.MainScreen), true)
        .setLaunchSingleTop(true)
        .build()
}
fun destination(destinationScreen: Destinations): String = when (destinationScreen) {
    Destinations.MainScreen -> "main_screen"
    Destinations.Settings -> "settings"
    Destinations.Contacts -> "contacts"
    Destinations.SingleChat -> "single_chat"
    Destinations.Account -> "account"
    Destinations.Registration -> "registration"
    Destinations.Check -> "check"
    Destinations.AppSettings -> "app_settings"
}









