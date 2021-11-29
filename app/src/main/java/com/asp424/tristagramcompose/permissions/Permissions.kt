package com.asp424.tristagramcompose.permissions

import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.asp424.tristagramcompose.MainActivity


const val WRITE_PERM = android.Manifest.permission.WRITE_EXTERNAL_STORAGE
const val READ_CONTACTS = android.Manifest.permission.READ_CONTACTS

fun checkPermissions(
    permission: String,
    mainActivity: MainActivity
): Boolean {
    return if (ContextCompat.checkSelfPermission(
            mainActivity,
            permission
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        ActivityCompat.requestPermissions(
            mainActivity,
            arrayOf(permission),
            200
        )
        false
    } else true
}
