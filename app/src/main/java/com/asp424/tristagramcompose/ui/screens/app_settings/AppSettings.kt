package com.asp424.tristagramcompose.ui.screens.app_settings

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asp424.tristagramcompose.MainActivity
import com.asp424.tristagramcompose.ui.screens.cells.BaseScreen
import com.asp424.tristagramcompose.viewmodels.SettingsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@SuppressLint("ServiceCast")
@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun AppSettings(settingsViewModel: SettingsViewModel = viewModel(
    factory = (LocalContext.current as MainActivity).viewModelFactory.get()
)
) {
    BaseScreen(
        headerText = "AppSettings",
        verticalArrangement = Arrangement.Center,
        inRow = {},
        inColumn = {
            Button(
                onClick = {},
                Modifier.padding(bottom = 16.dp)
            ) {
                Text(text = "Отключить уведомления", fontSize = 20.sp)
            }
        },
        onBackPressed = { settingsViewModel.goToSettings() },
        onSettingIconPressed = {}, userName = { _, _, _ -> })
}