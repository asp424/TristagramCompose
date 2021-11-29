package com.asp424.tristagramcompose.data.repos.registration

import com.asp424.tristagramcompose.MainActivity
import com.asp424.tristagramcompose.data.firebase.datasources.RegistrationDataSource
import javax.inject.Inject

class RegistrationRepo @Inject constructor(registration: RegistrationDataSource) : RegistrationInterface {
    private val netDataSource = registration
    override fun checkForAuth(result: (Boolean) -> Unit) {
        netDataSource.checkForAuth { res ->
            result(res)
        }
    }

    override fun signOut(){
        netDataSource.signOut()
    }

    override fun startRegistrationByPhone(
        mainActivityComponent: MainActivity,
        phoneNumber: String,
        result: (Int) -> Unit
    ) {
        netDataSource.startRegistrationByPhone(
            mainActivityComponent = mainActivityComponent,
            phoneNumber = phoneNumber,
        ) { state ->
            result(state)
        }
    }

    override fun startAuthorisationByPhone(code: String, result: (Int) -> Unit) {
        netDataSource.startAuthorisationByPhone(code = code) { res ->
            result(res)
        }
    }
}