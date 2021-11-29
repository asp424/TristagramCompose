package com.asp424.tristagramcompose.notification.remote_message.retrofit

import com.asp424.tristagramcompose.data.firebase.models.UserModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.POST

interface ApiService {
    @POST("send")
    fun sendRemoteMessage(
        @HeaderMap headers: HashMap<String, String>,
        @Body remoteBody: String?
    ): Call<String?>?
}