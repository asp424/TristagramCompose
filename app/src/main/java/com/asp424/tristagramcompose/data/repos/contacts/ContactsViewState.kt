package com.asp424.tristagramcompose.data.repos.contacts

import com.asp424.tristagramcompose.data.firebase.models.UserModel
import com.google.firebase.database.DataSnapshot

sealed class ContactsResponse {
    data class Changed(val contact: UserModel) : ContactsResponse()
    data class ChangedList(val contacts: HashMap<String, UserModel>) : ContactsResponse()
    data class ChangedUpdate(val snapshot: DataSnapshot) : ContactsResponse()
    data class Cancelled(val error: String) : ContactsResponse()
    object Empty : ContactsResponse()
    object Loading : ContactsResponse()

    val result: Any
        get() = when (this) {
            is Changed -> {
                contact
            }
            is ChangedUpdate -> {
                snapshot
            }
            is Cancelled -> {
                error
            }
            is ChangedList -> {
                contacts
            }
            else -> {}
        }
}