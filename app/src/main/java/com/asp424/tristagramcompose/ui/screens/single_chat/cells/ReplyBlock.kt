package com.asp424.tristagramcompose.ui.screens.single_chat.cells

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Reply
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asp424.tristagramcompose.R
import com.asp424.tristagramcompose.ui.screens.cells.animations.animatedWidth
import com.asp424.tristagramcompose.ui.theme.H7
import com.asp424.tristagramcompose.ui.theme.Navy500_1
import com.asp424.tristagramcompose.ui.theme.Navy700_1
import com.asp424.tristagramcompose.viewmodels.SingleChatViewModel
import kotlinx.coroutines.*

@OptIn(ExperimentalComposeUiApi::class)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ReplyBlock(singleChatViewModel: SingleChatViewModel) {
    val big by singleChatViewModel.stateReplyBlock.observeAsState(false)
    val text by singleChatViewModel.textMessageReply.observeAsState("")
    val color = if (!isSystemInDarkTheme()) Navy700_1 else Navy500_1
    val coroutine = rememberCoroutineScope()
    Card(
        modifier = Modifier
            .animatedWidth(big = big)
            .padding(end = 5.dp, bottom = 3.dp, start = 4.dp), shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, Gray)
    ) {
        Row( verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .height(40.dp),
            horizontalArrangement = Arrangement.SpaceBetween) {
            Icon(Icons.Outlined.Reply, contentDescription = null,
                modifier = Modifier.graphicsLayer {
                    rotationX = 180f
                    rotationZ = 180f
                }, tint = color
            )
            Icon(Icons.Outlined.Close, contentDescription = null, tint = Gray,
                modifier = Modifier.clickable {
                    singleChatViewModel.apply {
                        coroutine.launch(Dispatchers.Main) {
                            delay(200L)
                           this@apply.reply.value = false
                        }
                        setDataForReply("", "",
                            stateReplyBlockValue = false)
                    }
                })
        }
            Row(horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 43.dp, top = 10.dp, bottom = 10.dp, end = 43.dp),
                verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier
                ){
                    Text(text = singleChatViewModel.nameForReply.value!!, style = H7,
                        fontSize = 16.sp, color = color)
                    Text(text = text, style = TextStyle(
                        fontFamily =  FontFamily(Font(R.font.merriweatherregular))
                    ), maxLines = 1)
                }
            }
    }
}