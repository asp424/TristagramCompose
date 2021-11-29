package com.asp424.tristagramcompose.ui.screens.single_chat.cells

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Attachment
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asp424.tristagramcompose.R
import com.asp424.tristagramcompose.ui.theme.MyTheme
import com.asp424.tristagramcompose.ui.theme.Navy500_1
import com.asp424.tristagramcompose.ui.theme.Navy700_1
import com.asp424.tristagramcompose.viewmodels.SingleChatViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SendBlockReply(singleChatViewModel: SingleChatViewModel, onSendMessage: () -> Unit) {
    val cont = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    var text by remember {
        mutableStateOf("")
    }
    var icon by remember {
        mutableStateOf(Icons.Default.Attachment)
    }
    var enableSend by remember {
        mutableStateOf(true)
    }
    MyTheme {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(10.dp), verticalAlignment = Alignment.CenterVertically
        ) {
                TextField(
                    value = text,
                    onValueChange = { new ->
                        text = new
                        singleChatViewModel.apply {
                            icon = if (text.isNotEmpty()) {
                                updateStateTyping()
                                Icons.Default.Send
                            } else {
                                updateStateOnLine()
                                Icons.Default.Attachment
                            }
                        }
                    },
                    Modifier
                        .width(LocalConfiguration.current.screenWidthDp.dp - 150.dp)
                        .padding(end = 3.dp)
                        .focusRequester(focusRequester),
                    shape = RoundedCornerShape(20.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.Black,
                        disabledTextColor = Color.Transparent,
                        backgroundColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),

                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    textStyle = TextStyle(
                        fontFamily = FontFamily(
                            Font(R.font.merriweatherregular)
                        ), fontSize = 16.sp, textAlign = TextAlign.Start
                    )
                )
                Card(
                    border = BorderStroke(
                        2.dp,
                        if (!isSystemInDarkTheme()) Navy700_1 else Navy500_1
                    ), shape = RoundedCornerShape(20.dp)
                ) {
                    Icon(icon,
                        contentDescription = null,
                        modifier = Modifier
                            .clickable(enabled = enableSend) {
                                cont?.hide()
                                onSendMessage()
                                if (icon == Icons.Default.Send) {
                                    enableSend = false
                                    if (text.isNotEmpty()) {
                                        singleChatViewModel.updateStateOnLine()
                                        text = ""
                                        icon = Icons.Default.Attachment
                                        enableSend = true
                                      //  singleChatViewModel.sendTextMessage(text, "chat") {

                                        //}
                                    } else { // warning
                                    }
                                }
                            }
                            .size(45.dp)
                            .padding(10.dp),
                        tint = if (!isSystemInDarkTheme()) Navy700_1 else Navy500_1)
                }
                Card(
                    border = BorderStroke(
                        2.dp,
                        if (!isSystemInDarkTheme()) Navy700_1 else Navy500_1
                    ), shape = RoundedCornerShape(20.dp)
                ) {
                    Icon(
                        Icons.Default.Mic,
                        contentDescription = null,
                        modifier = Modifier
                            .clickable {

                            }
                            .size(45.dp)
                            .padding(10.dp),
                        tint = if (!isSystemInDarkTheme()) Navy700_1 else Navy500_1
                    )
                }
        }
    }
    DisposableEffect(Unit) {
        focusRequester.requestFocus()
        onDispose {
        }
    }
}