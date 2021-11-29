package com.asp424.tristagramcompose.ui.screens.single_chat.cells

import android.service.autofill.OnClickAction
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Reply
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asp424.tristagramcompose.R
import com.asp424.tristagramcompose.ui.theme.Navy500_1
import com.asp424.tristagramcompose.ui.theme.Navy700
import com.asp424.tristagramcompose.ui.theme.Navy700_1
import com.asp424.tristagramcompose.utils.asTime
import com.asp424.tristagramcompose.viewmodels.SingleChatViewModel

@Composable
fun TextMessageCardReply(
    side: Alignment.Horizontal,
    singleChatViewModel: SingleChatViewModel,
    border: Dp,
    color: Color,
    corners: RoundedCornerShape,
    timeStamp: String?,
    text: String?, onClickAction: (Boolean) -> Unit
) {
    Column(
        horizontalAlignment = side,
        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp, top = 15.dp)
            .width((LocalConfiguration.current.screenWidthDp.dp))
            .wrapContentHeight()
            .clickable { onClickAction(true) }
    ) {
        Card(
            backgroundColor = Navy700,
            border = BorderStroke(border, color), shape = corners,
        ) {
            Column(Modifier.fillMaxWidth()) {
                Row(Modifier.fillMaxWidth()) {
                    if (text!!.isNotEmpty())
                        Text(
                            text = text, fontSize = 16.sp, style = TextStyle(
                                fontFamily = FontFamily(
                                    Font(R.font.merriweatherregular)
                                )
                            ), modifier = Modifier
                                .wrapContentHeight()
                                .padding(10.dp)
                                .width((LocalConfiguration.current.screenWidthDp.dp) * 2 / 3)
                        )
                    Icon(
                        Icons.Default.Reply,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(10.dp),
                        tint = if (!isSystemInDarkTheme()) Navy700_1 else Navy500_1
                    )
                }
                SendBlockReply(singleChatViewModel = singleChatViewModel, onSendMessage = {onClickAction(true)})
            }
        }
        Text(
            text = timeStamp!!.asTime(),
            color = if (!isSystemInDarkTheme()) Navy700_1 else Navy500_1
        )
    }
}