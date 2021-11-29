package com.asp424.tristagramcompose.navigator


import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


interface Navigator {
    val navActions: StateFlow<NavigationAction?>
    fun navigate(navAction: NavigationAction?)
}

class CustomNavigator @Inject constructor() : Navigator {
    private val _navActions: MutableStateFlow<NavigationAction?> by lazy {
        MutableStateFlow(null)
    }
    override val navActions = _navActions.asStateFlow()

    override fun navigate(navAction: NavigationAction?) {
        _navActions.value = navAction
    }
}