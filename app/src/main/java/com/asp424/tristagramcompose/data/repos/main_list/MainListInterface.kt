package com.asp424.tristagramcompose.data.repos.main_list

import com.asp424.tristagramcompose.data.firebase.models.PhoneContactModel
import kotlinx.coroutines.flow.Flow

interface MainListInterface {
    suspend fun getCurrentUserModel(result: (Int) -> Unit)
    suspend fun updateContacts(listContacts: MutableList<PhoneContactModel>, result: (Int) -> Unit)
    suspend fun updateStateExit(function: ()-> Unit)
    suspend fun updateStateOnLine()
    suspend fun checkForAuth(result: (Boolean) -> Unit)
}