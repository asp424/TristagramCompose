package com.asp424.tristagramcompose.data.repos.registration

import com.asp424.tristagramcompose.MainActivity

interface RegistrationInterface {
    fun checkForAuth(result: (Boolean) -> Unit)
    fun signOut()
    fun startRegistrationByPhone(
        mainActivityComponent: MainActivity,
        phoneNumber: String,
        result: (Int) -> Unit
    )

    fun startAuthorisationByPhone(code: String, result: (Int) -> Unit)
}