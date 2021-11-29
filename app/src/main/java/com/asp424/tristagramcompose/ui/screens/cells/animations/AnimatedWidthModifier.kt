package com.asp424.tristagramcompose.ui.screens.cells.animations

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

fun Modifier.animatedWidth(big: Boolean): Modifier = composed {
    width(
        (animateDpAsState(
            if (big)
                LocalConfiguration.current.screenWidthDp.dp else 0.dp
        )).value
     )
}



