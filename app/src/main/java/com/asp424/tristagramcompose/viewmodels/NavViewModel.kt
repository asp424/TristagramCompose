package com.asp424.tristagramcompose.viewmodels

import androidx.lifecycle.ViewModel
import com.asp424.tristagramcompose.navigator.CustomNavigator
import com.asp424.tristagramcompose.navigator.NavigationAction
import javax.inject.Inject

class NavViewModel
@Inject constructor(private val navigator: CustomNavigator) : ViewModel() {
    fun toScreen(destination: NavigationAction) {
        navigator.navigate(destination)
    }
}