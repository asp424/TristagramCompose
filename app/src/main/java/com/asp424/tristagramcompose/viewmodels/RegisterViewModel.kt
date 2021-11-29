package com.asp424.tristagramcompose.viewmodels

import androidx.lifecycle.*
import com.asp424.tristagramcompose.MainActivity
import com.asp424.tristagramcompose.data.repos.registration.RegistrationInterface
import com.asp424.tristagramcompose.data.repos.registration.RegistrationRepo
import com.asp424.tristagramcompose.navigator.CustomNavigator
import com.asp424.tristagramcompose.navigator.MainScreenAct
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class RegisterViewModel
@Inject constructor(private var repository: RegistrationRepo, private val navigator: CustomNavigator) : ViewModel(),
    RegistrationInterface {
    override fun checkForAuth(result: (Boolean) -> Unit) {
        repository.checkForAuth { auth ->
                result(auth)
            }
    }

    override fun signOut() {
        repository.signOut()
    }

    override fun startRegistrationByPhone(
        mainActivityComponent: MainActivity,
        phoneNumber: String,
        result: (Int) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.startRegistrationByPhone(
                mainActivityComponent = mainActivityComponent,
                phoneNumber = phoneNumber
            ) { state ->
                result(state)
            }
        }

    }

    override fun startAuthorisationByPhone(code: String, result: (Int) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.startAuthorisationByPhone(code) { res ->
                result(res)
            }
        }
    }

    fun goToMainScreen(){
        navigator.navigate(MainScreenAct)
    }
}