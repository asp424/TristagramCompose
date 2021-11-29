package com.asp424.tristagramcompose.data.repos.contacts

import com.asp424.tristagramcompose.data.firebase.datasources.ContactsDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ContactsRepo @Inject constructor(private val contacts: ContactsDataSource): ContactsInterface {
    private val netContacts = contacts
    @ExperimentalCoroutinesApi
    override suspend fun getContacts(): Flow<ContactsResponse>{
      return netContacts.getContacts()
    }
}