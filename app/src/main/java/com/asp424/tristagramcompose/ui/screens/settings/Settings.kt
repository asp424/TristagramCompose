package com.asp424.tristagramcompose.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
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

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun Settings(
    settingsViewModel: SettingsViewModel = viewModel(
        factory = (LocalContext.current as MainActivity).viewModelFactory.get()
    )
) {
    BaseScreen(
        headerText = "Settings",
        verticalArrangement = Arrangement.Center,
        inRow = {},
        inColumn = {
            Button(
                onClick = { settingsViewModel.goToContacts() },
                Modifier.padding(bottom = 16.dp)
            ) {
                Text(text = "Контакты", fontSize = 30.sp)
            }
            Button(
                onClick = { settingsViewModel.goToAppSettings() },
                Modifier.padding(bottom = 16.dp)
            ) {
                Text(text = "Приложение", fontSize = 30.sp)
            }
            Button(
                onClick = { settingsViewModel.goToAccountScreen() },
                Modifier.padding(bottom = 16.dp)
            ) {
                Text(text = "Мои данные", fontSize = 30.sp)
            }
        },
        onBackPressed = { settingsViewModel.goToMainScreen() },
        onSettingIconPressed = {}, userName = { _, _, _ -> })
}
