package com.asp424.tristagramcompose.ui.screens.single_chat.cells

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.asp424.tristagramcompose.data.room.single_chat.MessageModelRoom
import com.asp424.tristagramcompose.ui.screens.cells.animations.ResizeAnim
import com.asp424.tristagramcompose.ui.theme.*
import com.asp424.tristagramcompose.utils.WAS_READING
import com.asp424.tristagramcompose.viewmodels.SingleChatViewModel
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterialApi::class, androidx.compose.ui.ExperimentalComposeUiApi::class,
    androidx.compose.animation.ExperimentalAnimationApi::class
)
@Composable
fun TextMessageBlock(
    item: MessageModelRoom,
    singleChatViewModel: SingleChatViewModel,
    userName: String,
    from: String,
    currentUid: String = FirebaseAuth.getInstance().currentUser?.uid.toString(),
    visibleChange: (Boolean) -> Unit
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    var color by remember { mutableStateOf(Navy700_1) }
    var border by remember { mutableStateOf(1.dp) }
    var side by remember { mutableStateOf(Alignment.End) }
    var textAlign by remember { mutableStateOf(Alignment.CenterEnd) }
    var corners by remember {
        mutableStateOf(RoundedCornerShape(10.dp, 0.dp, 10.dp, 10.dp))
    }
    item.apply {
        if (from == currentUid
        ) {
            corners = RoundedCornerShape(10.dp, 0.dp, 10.dp, 10.dp)
            side = Alignment.End
            textAlign = Alignment.CenterEnd
            if (was_reading == WAS_READING) {
                color = if (!isSystemInDarkTheme()) Eye else Green900
                border = 2.dp
            } else {
                color = if (!isSystemInDarkTheme()) Navy700_1 else Navy500_1
                border = 1.dp
            }
        } else {
            textAlign = Alignment.CenterStart
            side = Alignment.Start
            corners = RoundedCornerShape(0.dp, 10.dp, 10.dp, 10.dp)
            color = if (!isSystemInDarkTheme()) Navy700_1 else Navy500_1
            border = 1.dp
            singleChatViewModel.setWasReading(id)
        }
        ResizeAnim(inExpanded = {
            TextMessageCardReply(side,
            singleChatViewModel, border, color, corners, timeStamp, text){
            expanded = !expanded
            visibleChange(true)
        }},
            inUnExpanded = {TextMessageCard(side, textAlign,
                singleChatViewModel, border, color, corners, timeStamp, text, userName, from){
                expanded = !expanded
                visibleChange(false) }},
                expanded = expanded)
    }

}
