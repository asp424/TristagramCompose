package com.asp424.tristagramcompose.ui.screens.single_chat.cells

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Reply
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asp424.tristagramcompose.R
import com.asp424.tristagramcompose.ui.theme.Navy500_1
import com.asp424.tristagramcompose.ui.theme.Navy700_1
import com.asp424.tristagramcompose.utils.USER
import com.asp424.tristagramcompose.utils.asTime
import com.asp424.tristagramcompose.utils.log
import com.asp424.tristagramcompose.viewmodels.SingleChatViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class, androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun TextMessageCard(
    side: Alignment.Horizontal,
    textAlign: Alignment,
    singleChatViewModel: SingleChatViewModel,
    border: Dp,
    color: Color,
    corners: RoundedCornerShape,
    timeStamp: String?,
    text: String?, userName: String,
    from: String, onClickAction: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    var scale by remember { mutableStateOf(0f) }
    val state = rememberTransformableState { zoomChange, _, _ ->
        scale *= zoomChange

    }
    var scaleMessage by remember { mutableStateOf(1f) }
    val stateMessage = rememberTransformableState { zoomChange, _, _ ->
        scaleMessage *= zoomChange

    }
    val coroutine = rememberCoroutineScope()
    val swipeableState = rememberSwipeableState(0)
    val sizePx = with(LocalDensity.current) { -60.dp.toPx() }
    val anchors = mapOf(0f to 0, sizePx to 1)
    LaunchedEffect(swipeableState.offset.value) {
        scale = -swipeableState.offset.value / 76
    }

    if (swipeableState.currentValue == 1) {
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        LaunchedEffect(swipeableState) {
            swipeableState.animateTo(0)
        }
        singleChatViewModel.apply {
            this.reply.value = true
            keyBoardState.value = true
            setDataForReply(
                text,
                if (from == USER.id) USER.fullname
                else userName ?: "",
                stateReplyBlockValue = true
            )
        }
    }
    var heightCard by remember {
        mutableStateOf(0.dp)
    }
    var ckick by remember {
        mutableStateOf(false)
    }
    var stop by remember {
        mutableStateOf(false)
    }
    var heightCol by remember {
        mutableStateOf(0.dp)
    }
    val aHeight by animateDpAsState(
        if (ckick)
            0.dp
        else heightCol
    )
    val pad by animateDpAsState(
        if (ckick)
            0.dp
        else 20.dp
    )
    var width by remember {
        mutableStateOf(0.dp)
    }
    val localDens = LocalDensity.current
    Row(
        Modifier
            .fillMaxWidth()
            .height(heightCard)
                ,
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Outlined.Reply, contentDescription = null,
            modifier = Modifier
                .transformable(state = state)
                .graphicsLayer {
                    scaleY = scale
                    scaleX = scale
                }
                .padding(end = 10.dp), tint = color
        )
    }
    Column(
            horizontalAlignment = side,
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned {
                    coroutine.launch {
                        delay(100L)
                        if (!stop) {
                            heightCol = it.size.height.dp
                            stop = true
                        }
                    }
                }
                .wrapContentSize(align = textAlign)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = {
                            // onClickAction()
                                      },
                        onDoubleTap = {
                            ckick = true
                        })
                }
                .padding(bottom = pad)
                 .height(aHeight)
                .width((LocalConfiguration.current.screenWidthDp.dp / 5) * 4)
        ) {
            Card(
                border = BorderStroke(border, color), shape = corners,
                modifier = Modifier
                    .swipeable(
                        state = swipeableState,
                        anchors = anchors,
                        thresholds = { _, _ -> FractionalThreshold(0.3f) },
                        orientation = Orientation.Horizontal
                    )
                    .onGloballyPositioned {
                        heightCard = it.size.height.dp / localDens.density
                        width = it.size.width.dp / localDens.density
                    }
                    .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }
            ) {
                if (text!!.isNotEmpty())
                    Text(
                        text = text, fontSize = 16.sp, style = TextStyle(
                            fontFamily = FontFamily(
                                Font(R.font.merriweatherregular)
                            )
                        ), modifier = Modifier
                            .padding(8.dp)
                    )
            }
            Text(
                text = timeStamp!!.asTime(),
                color = if (!isSystemInDarkTheme()) Navy700_1 else Navy500_1,
                modifier = Modifier
                    .swipeable(
                        state = swipeableState,
                        anchors = anchors,
                        thresholds = { _, _ -> FractionalThreshold(0.3f) },
                        orientation = Orientation.Horizontal
                    )
                    .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }
            )
        }
}