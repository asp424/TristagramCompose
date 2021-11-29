package com.asp424.tristagramcompose.data.firebase.datasources

import android.net.Uri
import android.util.Log
import com.asp424.tristagramcompose.data.repos.settings.SettingsInterface
import com.asp424.tristagramcompose.utils.CHILD_PHOTO_URL
import com.asp424.tristagramcompose.utils.NODE_USERS
import com.asp424.tristagramcompose.utils.USER
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import javax.inject.Inject

class SettingsDataSource @Inject constructor() : SettingsInterface {
    override suspend fun putPhotoToStorage(uri: Uri, folder: String, function: (String) -> Unit) {
        val path = FirebaseStorage.getInstance().reference.child(folder)
            .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
             path.putFile(uri)
            .addOnSuccessListener {
                path.downloadUrl.addOnSuccessListener { urine ->
                    FirebaseDatabase.getInstance().reference.child(NODE_USERS)
                        .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
                        .child(CHILD_PHOTO_URL).setValue(urine.toString())
                        .addOnSuccessListener {
                            USER.photoUrl = urine.toString()
                            function(urine.toString())
                        }
                        .addOnFailureListener {}
                }
                    .addOnFailureListener {}
            }
            .addOnFailureListener {}
    }
}