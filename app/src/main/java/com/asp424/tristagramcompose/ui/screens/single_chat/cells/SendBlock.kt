package com.asp424.tristagramcompose.ui.screens.single_chat.cells

import android.annotation.SuppressLint
import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.getSystemService
import com.asp424.tristagramcompose.R
import com.asp424.tristagramcompose.ui.theme.MyTheme
import com.asp424.tristagramcompose.ui.theme.Navy500_1
import com.asp424.tristagramcompose.ui.theme.Navy700_1
import com.asp424.tristagramcompose.utils.USER
import com.asp424.tristagramcompose.utils.log
import com.asp424.tristagramcompose.viewmodels.SingleChatViewModel
import com.google.accompanist.insets.LocalWindowInsets


@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalComposeUiApi::class, androidx.compose.animation.ExperimentalAnimationApi::class)
@Composable
fun SendBlock(
    singleChatViewModel: SingleChatViewModel,
    onTextFieldFocused: () -> Unit,
    focusRequester: FocusRequester,
    token: String?
) {
    val imm = LocalContext.current.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(LocalView.current, InputMethodManager.SHOW_FORCED)
    val localDens = LocalDensity.current
    val heightScreen = with(localDens) { LocalConfiguration.current.screenHeightDp.dp.toPx() }
    var text by remember {
        mutableStateOf("")
    }
    var icon by remember {
        mutableStateOf(Icons.Default.Attachment)
    }
    var enableSend by remember {
        mutableStateOf(true)
    }

    val tint = if (!isSystemInDarkTheme()) Navy700_1 else Navy500_1
    MyTheme {
        Column(
          Modifier.fillMaxHeight()
                , verticalArrangement = Arrangement.Bottom
        ) {
            ReplyBlock(singleChatViewModel)
            Row(
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier
                    .padding(start = 4.dp, bottom = 1.dp)
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
                        .width(LocalConfiguration.current.screenWidthDp.dp - 120.dp)
                        .onFocusChanged {
                            onTextFieldFocused()
                        }
                        .onGloballyPositioned {
                            singleChatViewModel.height.value =
                                (heightScreen - it.localToRoot(
                                    Offset(
                                        0f,
                                        0f
                                    )
                                ).y) / localDens.density
                        }
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
                    border = BorderStroke(2.dp, tint),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.padding(end = 2.dp)
                ) {
                    Icon(
                        icon,
                        contentDescription = null,
                        modifier = Modifier
                            .clickable(enabled = enableSend) {
                                if (icon == Icons.Default.Send) {
                                    enableSend = false
                                    if (text.isNotEmpty()) {
                                        singleChatViewModel.sendTextMessage(message = text, token = token!!,
                                            fullName = USER.fullname, typeMessage = "chat", photoUrl = USER.photoUrl) {
                                            singleChatViewModel.updateStateOnLine()
                                            text = ""
                                            icon = Icons.Default.Attachment
                                            enableSend = true
                                        }
                                    }
                                }
                            }
                            .size(55.dp)
                            .padding(6.dp),
                        tint = tint
                    )
                }
                Card(
                    border = BorderStroke(
                        2.dp, tint
                    ), shape = RoundedCornerShape(20.dp)
                ) {
                    Icon(
                        Icons.Default.Mic,
                        contentDescription = null,
                        modifier = Modifier
                            .clickable {}
                            .size(55.dp)
                            .padding(6.dp),
                        tint = tint
                    )
                }
            }
        }

    }
}
