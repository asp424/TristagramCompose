package com.asp424.tristagramcompose.viewmodels

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.startForegroundService
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asp424.tristagramcompose.data.firebase.models.PhoneContactModel
import com.asp424.tristagramcompose.data.repos.main_list.MainListRepo
import com.asp424.tristagramcompose.navigator.CustomNavigator
import com.asp424.tristagramcompose.utils.initContacts
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel
@Inject constructor(private var repository: MainListRepo, private val navigator: CustomNavigator) : ViewModel(), DefaultLifecycleObserver {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        setOnLineState()
        viewModelScope.launch(Dispatchers.IO) {
            checkForAuth {
                if (it)
                    getCurrentUserModel { res ->
                        if (res == 1)
                            updateContacts(initContacts(owner as Context)) {

                            }
                    }
            }
        }
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        setExitState {}
    }

    fun updateContacts(listContacts: MutableList<PhoneContactModel>, result: (Int) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateContacts(listContacts = listContacts) { res ->
                result(res)
            }
        }
    }

    fun setExitState(function: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.updateStateExit {
                function()
            }
        }
    }

    suspend fun checkForAuth(result: (Boolean) -> Unit) {
        repository.checkForAuth { auth ->
            result(auth)
        }
    }

    fun setOnLineState() {
        CoroutineScope(Dispatchers.IO).launch {
            repository.updateStateOnLine()
        }
    }

    fun getCurrentUserModel(result: (Int) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.getCurrentUserModel { res ->
                result(res)
            }
        }
    }
}