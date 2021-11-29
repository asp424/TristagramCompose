package com.asp424.tristagramcompose.ui.screens.main_list.cells

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback

import androidx.compose.ui.unit.dp
import com.asp424.tristagramcompose.ui.theme.Navy500_1
import com.asp424.tristagramcompose.ui.theme.Navy700_1

@Composable
fun CellCard(onClick: () -> Unit, inCard: @Composable () -> Unit) {
    val haptic = LocalHapticFeedback.current
    Card(shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onClick() }
            .padding(start = 16.dp, end = 16.dp, top = 6.dp),
        elevation = 10.dp,
        border = BorderStroke(
            width = 1.dp,
            color = if (!isSystemInDarkTheme()) Navy700_1 else Navy500_1
        )) {
        inCard()
    }
}

