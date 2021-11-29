package com.asp424.tristagramcompose.ui.screens.regscreen.cells

import android.util.Log
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.asp424.tristagramcompose.ui.theme.H5
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow

@Composable
fun CellOutlinedField(
    value: String,
    text: String,
    function: (String) -> Unit
) {
    val interactionSource = remember {
        object : MutableInteractionSource {
            override val interactions = MutableSharedFlow<Interaction>(
                extraBufferCapacity = 16,
                onBufferOverflow = BufferOverflow.DROP_OLDEST,
            )

            override suspend fun emit(interaction: Interaction) {
                if (interaction is PressInteraction.Release) {

                }

                interactions.emit(interaction)
            }

            override fun tryEmit(interaction: Interaction): Boolean {
                return interactions.tryEmit(interaction)
            }
        }
    }
    OutlinedTextField(
        textStyle = H5,
        keyboardActions = KeyboardActions(onAny = {}),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        interactionSource = interactionSource,
        value = value,
        onValueChange = { aa ->
            function(aa)
        }, label = {
            Text(
                text = text,
                style = H5
            )
        },
        singleLine = true, modifier = Modifier
            .fillMaxWidth()
    )
}