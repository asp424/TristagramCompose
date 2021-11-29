package com.asp424.tristagramcompose.ui.screens.contacts.cells

import android.view.WindowManager
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asp424.tristagramcompose.ui.theme.Green900
import com.asp424.tristagramcompose.ui.theme.H7
import com.asp424.tristagramcompose.ui.theme.H8
import com.asp424.tristagramcompose.utils.ONLINE
import com.asp424.tristagramcompose.utils.TYPING
import com.asp424.tristagramcompose.utils.getLastMessageString
import com.asp424.tristagramcompose.utils.getStringWasForChat
import com.google.firebase.auth.FirebaseAuth


@Composable
fun NameAndStatusBlockContacts(
    fullName: String = "",
    state: String = ""
) {
    Column(
        verticalArrangement = Arrangement.Top,
        modifier = Modifier.padding(start = 10.dp)
    ) {
        Text(
            text = fullName,
            fontSize = 17.sp,
            style = H7,
            maxLines = 1, modifier = Modifier.padding(top = 3.dp)
        )
        Text(
            text = when (state) {
                ONLINE -> ONLINE
                TYPING -> TYPING
                else -> "был(а)" + getStringWasForChat(state.toLong())
            },
            fontSize = 14.sp,
            style = H8,
            color = if (!isSystemInDarkTheme()) Color.Black else Color.White,
            modifier = Modifier.padding(top = 3.dp)
        )
    }
}


