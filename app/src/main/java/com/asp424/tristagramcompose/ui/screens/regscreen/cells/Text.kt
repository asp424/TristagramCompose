package com.asp424.tristagramcompose.ui.screens.regscreen.cells

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asp424.tristagramcompose.ui.theme.H6

@Composable
fun CellTextAnnotation(text: String){
    Text(
        modifier = Modifier.padding(16.dp), text = text, textAlign = TextAlign.Center,
        fontSize = 18.sp
    )
}
@Composable
fun CellTextError(text: String){
    Text(
        text = text, fontSize = 25.sp, color = Color.Red, modifier = Modifier.padding(10.dp), textAlign = TextAlign.Center
    )
}