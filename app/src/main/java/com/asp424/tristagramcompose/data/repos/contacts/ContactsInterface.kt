package com.asp424.tristagramcompose.data.repos.contacts

import kotlinx.coroutines.flow.Flow

interface ContactsInterface {
suspend fun getContacts(): Flow<ContactsResponse>

}