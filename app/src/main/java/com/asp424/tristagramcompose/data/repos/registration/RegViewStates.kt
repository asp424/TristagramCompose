package com.asp424.tristagramcompose.data.repos.registration

sealed class RegViewStates {
data class AutoRegistrationSuccess(val smsCode: String): RegViewStates()
object StartRegistration: RegViewStates()
data class RegError(val message: String): RegViewStates()
data class OnCodeSent(val id: String): RegViewStates()

val extractData: String
    get() = when (this) {
        is AutoRegistrationSuccess -> smsCode
        is OnCodeSent -> id
        is RegError -> message
        is StartRegistration -> ""
    }
}