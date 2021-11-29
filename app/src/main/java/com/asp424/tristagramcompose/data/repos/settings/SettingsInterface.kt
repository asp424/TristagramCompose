package com.asp424.tristagramcompose.data.repos.settings

import android.net.Uri

interface SettingsInterface {
    suspend fun putPhotoToStorage(uri: Uri, folder: String, function: (String) -> Unit)
}