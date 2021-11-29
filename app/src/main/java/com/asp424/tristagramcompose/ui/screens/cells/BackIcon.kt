package com.asp424.tristagramcompose.ui.screens.cells

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardBackspace
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asp424.tristagramcompose.R
import com.asp424.tristagramcompose.navigator.MainScreenAct
import com.asp424.tristagramcompose.ui.theme.Navy500_1
import com.asp424.tristagramcompose.ui.theme.Navy700_1

@Composable
fun BackIcon(onClick: () -> Unit){
    Text(
        text = "<-",
        style = TextStyle(
            fontFamily = FontFamily(
                Font(R.font.ass)
            )
        ),
        modifier = Modifier
            .clickable {
                onClick()
            },
        fontSize = 50.sp,
        color = if (!isSystemInDarkTheme()) Navy700_1 else Navy500_1
    )
}
