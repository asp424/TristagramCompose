package com.asp424.tristagramcompose.data.repos.main_list

import com.asp424.tristagramcompose.data.firebase.datasources.MainListDataSource
import com.asp424.tristagramcompose.data.firebase.models.PhoneContactModel
import com.asp424.tristagramcompose.data.room.main_list.MainListDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainListRepo @Inject constructor(
    private val mainListDataSource: MainListDataSource,
    private val mainListDao: MainListDao
) {
    suspend fun getCurrentUserModel(result: (Int) -> Unit) {
        mainListDataSource.getCurrentUserModel {
            result(it)
        }
    }


    suspend fun updateContacts(
        listContacts: MutableList<PhoneContactModel>,
        result: (Int) -> Unit
    ) {
        mainListDataSource.updateContacts(listContacts = listContacts) { res ->
            result(res)
        }
    }

    suspend fun updateStateExit(function: () -> Unit) {
        mainListDataSource.updateStateExit {
            function()
        }
    }

    suspend fun updateStateOnLine() {
        mainListDataSource.updateStateOnLine()
    }

    suspend fun checkForAuth(result: (Boolean) -> Unit) {
        mainListDataSource.checkForAuth { res ->
            result(res)
        }
    }

    suspend fun getMainList(function: (MainListResponse) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            function(MainListResponse.OnSuccess(mainListDao.getAllItems()))
        }

        updateRoom {
            function(it)
        }
    }

    suspend fun updateRoom(function: (MainListResponse) -> Unit) {
        var count = 0
        mainListDataSource.getMainList().collect { response ->
            when (response) {
                is MainListResponse.OnSuccessHash ->
                    response.mainListHash.forEach { (_, value) ->
                        count++
                        mainListDao.insertOrUpdateItem(value)
                        if (response.mainListHash.size == count) {
                            count = 0
                            function(MainListResponse.OnSuccess(mainListDao.getAllItems()))
                        }
                    }
                is MainListResponse.OnError -> function(MainListResponse.OnError(response.message))
                else -> {
                }
            }
        }
    }

    fun deleteAllFromMainListRoom() {
        mainListDao.deleteAllFromMainList()
    }
}

