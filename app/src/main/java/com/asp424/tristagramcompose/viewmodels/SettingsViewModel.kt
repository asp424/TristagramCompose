package com.asp424.tristagramcompose.viewmodels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asp424.tristagramcompose.data.repos.settings.SettingsRepo
import com.asp424.tristagramcompose.navigator.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsViewModel
@Inject constructor(
    private var repository: SettingsRepo,
    private val navigator: CustomNavigator
) : ViewModel() {
    private val _uri = MutableLiveData("")
    val uri: LiveData<String> = _uri

    fun goToMainScreen() {
        navigator.navigate(MainScreenAct)
    }

    fun goToAccountScreen() {
        navigator.navigate(AccountAct)
    }

    fun goToContacts() {
        navigator.navigate(ContactsAct)
    }

    fun goToSettings() {
        navigator.navigate(SettingsAct)
    }

    fun goToAppSettings() {
        navigator.navigate(AppSettingsAct)
    }

    fun putPhotoToStorage(uri: Uri, folder: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.putPhotoToStorage(uri, folder) {
                _uri.postValue(it)
            }
        }
    }
}

