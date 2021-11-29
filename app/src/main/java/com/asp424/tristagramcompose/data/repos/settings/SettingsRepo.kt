package com.asp424.tristagramcompose.data.repos.settings

import android.net.Uri
import com.asp424.tristagramcompose.data.firebase.datasources.SettingsDataSource
import com.google.firebase.storage.StorageReference
import javax.inject.Inject

class SettingsRepo @Inject constructor(private val settingsDataSource: SettingsDataSource): SettingsInterface {
    override suspend fun putPhotoToStorage(uri: Uri, folder: String, function: (String) -> Unit) {
        settingsDataSource.putPhotoToStorage(uri, folder){
            function(it)
        }
    }
}