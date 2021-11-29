package com.asp424.tristagramcompose.data.repos.main_list

import com.asp424.tristagramcompose.data.room.main_list.MainModelRoom

sealed class MainListResponse {
    data class OnSuccess(val mainList: List<MainModelRoom>) : MainListResponse()
    data class OnSuccessHash(val mainListHash: HashMap<String, MainModelRoom>) : MainListResponse()
    data class OnError(val message: String) : MainListResponse()
    object Empty : MainListResponse()
    object Loading : MainListResponse()

    val extractData: Any
        get() = when (this) {
            is OnSuccess -> mainList
            is OnError -> message
            is OnSuccessHash -> mainListHash
            else -> {""}
        }
}
