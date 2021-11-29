package com.asp424.tristagramcompose.viewmodels

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asp424.tristagramcompose.data.repos.main_list.MainListRepo
import com.asp424.tristagramcompose.data.repos.main_list.MainListResponse
import com.asp424.tristagramcompose.navigator.CustomNavigator
import com.asp424.tristagramcompose.navigator.SettingsAct
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainScreenViewModel @Inject constructor(
    private var repository: MainListRepo,
    private val navigator: CustomNavigator
) : ViewModel(),
    DefaultLifecycleObserver {
    private val _users: MutableStateFlow<MainListResponse?> =
        MutableStateFlow(MainListResponse.Loading)
    val users = _users.asStateFlow()
    private var jobMessages: Job? = null

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        jobMessages?.cancel()
        jobMessages = viewModelScope.launch(Dispatchers.IO) {
            repository.getMainList {
                _users.value = it
            }
        }
        jobMessages?.start()
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        jobMessages?.cancel()
    }

    fun goToSettings() {
        navigator.navigate(SettingsAct)
    }

    fun deleteAllFromMainListRoom() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllFromMainListRoom()
        }
    }
}
