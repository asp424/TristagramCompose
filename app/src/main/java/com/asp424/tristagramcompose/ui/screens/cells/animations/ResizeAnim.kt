package com.asp424.tristagramcompose.ui.screens.cells.animations

import androidx.compose.animation.*
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntSize

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ResizeAnim(
    expanded: Boolean,
    inExpanded:@Composable (AnimatedVisibilityScope) -> Unit,
    inUnExpanded:@Composable (AnimatedVisibilityScope) -> Unit) {
    AnimatedContent(
        targetState = expanded,
        transitionSpec = {
            fadeIn(animationSpec = tween(150, 150)) with
                    fadeOut(animationSpec = tween(150)) using
                    SizeTransform { initialSize, targetSize ->
                        if (targetState) {
                            keyframes {
                                // Expand horizontally first.
                                IntSize(targetSize.width, initialSize.height) at 150
                                durationMillis = 300
                            }
                        } else {
                            keyframes {
                                // Shrink vertically first.
                                IntSize(initialSize.width, targetSize.height) at 150
                                durationMillis = 300
                            }
                        }
                    }
        }
    ) { targetExpanded ->
        if (targetExpanded) inExpanded(this)
        else inUnExpanded(this)
    }
}