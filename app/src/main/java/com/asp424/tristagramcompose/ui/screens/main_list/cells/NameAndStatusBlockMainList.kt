package com.asp424.tristagramcompose.ui.screens.main_list.cells


import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asp424.tristagramcompose.ui.theme.Green900
import com.asp424.tristagramcompose.ui.theme.H7
import com.asp424.tristagramcompose.ui.theme.H8
import com.asp424.tristagramcompose.utils.getLastMessageString
import com.google.firebase.auth.FirebaseAuth

@Composable
fun NameAndStatusBlockMainList(
    lastMessage: String = "",
    lastMessageTime: String = "",
    wasReading: String = "",
    fullName: String = "",
    from: String = ""
) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(start = 10.dp)
    ) {
        Row(
            Modifier.fillMaxWidth().padding(
                end = 6.dp
            ), horizontalArrangement =
            Arrangement.SpaceBetween
        ) {
            Text(
                text = fullName,
                fontSize = 16.sp,
                style = H7,
                maxLines = 1, modifier = Modifier.width(LocalConfiguration
                    .current.screenWidthDp.dp / 2.3f))
            Row {
                if (wasReading.isNotEmpty())
                    Icon(
                        Icons.Default.Visibility,
                        contentDescription = null,
                        tint = Green900,
                        modifier = Modifier.size(20.dp))
                else {
                    if (from == FirebaseAuth.getInstance().currentUser?.uid.toString())
                        Icon(
                            Icons.Default.VisibilityOff,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier
                                .size(20.dp)
                        )
                }
                if (lastMessageTime.isNotEmpty())
                    Text(
                        text = getLastMessageString(lastMessageTime.toLong()),
                        fontSize = 14.sp,
                        style = H8,
                        maxLines = 1, modifier = Modifier.padding(start = 6.dp))
            }
        }
        Row(
            Modifier.fillMaxWidth(), horizontalArrangement =
            Arrangement.SpaceBetween
        ) {
            Text(
                text = lastMessage,
                fontSize = 14.sp,
                style = H8,
                maxLines = 1,
                modifier = Modifier
                    .padding(top = 6.dp)
                    .width(
                        LocalConfiguration.current.screenWidthDp.dp / 2
                    )
            )
            //countMessages
        }
    }
}