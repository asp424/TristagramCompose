package com.asp424.tristagramcompose.utils

import android.content.Context
import android.provider.ContactsContract
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.MeasureScope
import com.asp424.tristagramcompose.MainActivity
import com.asp424.tristagramcompose.data.firebase.models.PhoneContactModel
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


fun initContacts(context: Context?): MutableList<PhoneContactModel> {
    val listContacts = mutableListOf<PhoneContactModel>()
    val cursor = context?.contentResolver?.query(
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
        null,
        null,
        null,
        null
    )
    cursor?.let { cur ->
        cur.apply {
            while (moveToNext()) {
                var phoneVal =
                    getString(
                        getColumnIndexOrThrow(
                            ContactsContract
                                .CommonDataKinds.Phone.NUMBER
                        )
                    )
                        .filter { it.isDigit() }
                phoneVal = if (phoneVal.substring(0, 1) == "8")
                    "+7" + phoneVal.substring(1, 11)
                else "+$phoneVal"
                if (phoneVal != USER.phone && phoneVal.length == 12) {
                    PhoneContactModel().apply {
                        fullname =
                            getString(
                                getColumnIndexOrThrow(
                                    ContactsContract
                                        .Contacts.DISPLAY_NAME
                                )
                            )
                        phone = phoneVal
                        listContacts.add(this)
                    }
                }
            }
        }
        cursor.close()
    }
    return listContacts
}

fun String.asTime(): String {
    val time = Date(this.toLong())
    val timeFormat = SimpleDateFormat("H:mm", Locale.getDefault())
    return timeFormat.format(time)
}

fun String.asDate(): String {
    val time = Date(this.toLong())
    val timeFormat = SimpleDateFormat("dd:MM:yy", Locale.getDefault())
    return timeFormat.format(time)
}

fun getStringWasForChat(wasDate: Long): String {
    val timeWas = wasDate.toString().asTime()
    val dateNow = Calendar.getInstance(Locale.getDefault())
    val monthLitWas = formatDate("MMM", wasDate)
    val monthNumWas = formatDate("MM", wasDate)
    val yearWas = formatDate("yyyy", wasDate)
    val yearWasSmall = formatDate("yy", wasDate)
    val dayOfMonthWas = formatDate("d", wasDate)
    val dayOfYearWas = formatDate("D", wasDate)
    val yearNow = dateNow.get(Calendar.YEAR)
    return when (dateNow.get(Calendar.DAY_OF_YEAR) - dayOfYearWas.toInt()) {
        1 -> " вчера в $timeWas"
        2 -> " позавчера в $timeWas"
        0 -> " в $timeWas"
        else -> {
            when (yearNow - yearWas.toInt()) {
                1 -> " $dayOfMonthWas.$monthNumWas.$yearWasSmall в $timeWas"
                0 -> " $dayOfMonthWas $monthLitWas в $timeWas"
                else -> " в $yearWas году"
            }
        }
    }
}

private fun formatDate(value: String, date: Long): String {
    return SimpleDateFormat(value, Locale.getDefault()).format(date)
}

fun getLastMessageString(wasDate: Long): String {
    val timeWas = wasDate.toString().asTime()
    val dateNow = Calendar.getInstance(Locale.getDefault())
    val dayOfWeekWas = formatDate("E", wasDate)
    val monthLitWas = formatDate("MMM", wasDate)
    val yearWasSmall = formatDate("yy", wasDate)
    val monthNumWas = formatDate("MM", wasDate)
    val yearWas = formatDate("yyyy", wasDate)
    val dayOfMonthWas = formatDate("d", wasDate)
    val dayOfYearWas = formatDate("D", wasDate)
    val yearNow = dateNow.get(Calendar.YEAR)
    return when (dateNow.get(Calendar.DAY_OF_YEAR) - dayOfYearWas.toInt()) {
        in 1..6 -> dayOfWeekWas
        0 -> timeWas
        else -> when (yearNow - yearWas.toInt()) {
            0 -> "$dayOfMonthWas $monthLitWas"
            else -> "$dayOfMonthWas.$monthNumWas.$yearWasSmall"
        }
    }
}

fun changePhotoUser(context: MainActivity) = CoroutineScope(Dispatchers.IO).launch {
    context.selectImageLauncher.launch(
        CropImage.activity()
            .setAspectRatio(1, 1)
            .setRequestedSize(600, 600)
            .setCropShape(CropImageView.CropShape.OVAL)
            .getIntent(context)
    )
}

fun log(value: Any) {
    Log.d("My", value.toString())
}






