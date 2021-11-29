package com.asp424.tristagramcompose.viewmodels

import androidx.lifecycle.*
import com.asp424.tristagramcompose.data.repos.contacts.ContactsRepo
import com.asp424.tristagramcompose.data.repos.contacts.ContactsResponse
import com.asp424.tristagramcompose.navigator.CustomNavigator
import com.asp424.tristagramcompose.navigator.MainScreenAct
import com.asp424.tristagramcompose.navigator.SettingsAct
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import javax.inject.Inject


@OptIn(ExperimentalCoroutinesApi::class)
class ContactsViewModel
@Inject constructor(private var repository: ContactsRepo,
                    private val navigator: CustomNavigator) : ViewModel(),
    DefaultLifecycleObserver {
    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        viewModelScope.launch(Dispatchers.Main + job) {
            withContext(Dispatchers.IO){
                repository.getContacts().collect { res ->
                    kickCompose.postValue(count++)
                    _contacts.value = res
                }
            }
        }
    }









    val kickCompose = MutableLiveData(0)
    var count = 0
    private val _contacts: MutableStateFlow<ContactsResponse?> = MutableStateFlow(ContactsResponse.Loading)
    val contacts = _contacts.asStateFlow()
    private val job = SupervisorJob()


    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        job.cancel()
        count = 0
    }

    fun goToMainScreen(){
        navigator.navigate(MainScreenAct)
    }

    fun goToSettings(){
        navigator.navigate(SettingsAct)
    }
}

