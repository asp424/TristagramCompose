package com.asp424.tristagramcompose.ui.screens.regscreen

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Update
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asp424.tristagramcompose.MainActivity
import com.asp424.tristagramcompose.R
import com.asp424.tristagramcompose.ui.screens.cells.Visibility
import com.asp424.tristagramcompose.ui.screens.regscreen.cells.CellOutlinedField
import com.asp424.tristagramcompose.ui.screens.regscreen.cells.CellTextAnnotation
import com.asp424.tristagramcompose.ui.screens.regscreen.cells.CellTextError
import com.asp424.tristagramcompose.ui.theme.*
import com.asp424.tristagramcompose.utils.initContacts
import com.asp424.tristagramcompose.viewmodels.MainViewModel
import com.asp424.tristagramcompose.viewmodels.RegisterViewModel
import kotlinx.coroutines.*

val modifier = Modifier.fillMaxSize()

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("CoroutineCreationDuringComposition")
@ExperimentalCoroutinesApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun RegScreen(
    mainActivity: MainActivity,
    registerViewModel: RegisterViewModel,
    mainViewModel: MainViewModel = viewModel(
        factory = (LocalContext.current as MainActivity).viewModelFactory.get()
    )
) {
    var phoneNumber by rememberSaveable { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val coroutineScope1 = rememberCoroutineScope()
    val coroutineScopeError = rememberCoroutineScope()
    var code by remember { mutableStateOf("") }
    var textError by remember { mutableStateOf("") }
    var visible by remember { mutableStateOf(true) }
    var visible1 by remember { mutableStateOf(false) }
    var visible3 by remember { mutableStateOf(false) }
    var visible4 by remember { mutableStateOf(false) }
    var rotation by remember { mutableStateOf(0f) }
    var rotation1 by remember { mutableStateOf(0f) }
    var rotation2 by remember { mutableStateOf(0f) }
    var scale2 by remember { mutableStateOf(0f) }
    var scale by remember { mutableStateOf(1f) }
    var scale1 by remember { mutableStateOf(1f) }
    var scale3 by remember { mutableStateOf(1f) }
    var enable by remember { mutableStateOf(true) }
    var nav by remember { mutableStateOf(false) }
    var colorBar by remember { mutableStateOf(Red) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val state = rememberTransformableState { zoomChange, _, rotationChange ->
        scale *= zoomChange
        rotation += rotationChange
    }
    val state1 = rememberTransformableState { zoomChange, _, _ ->
        scale1 *= zoomChange
    }
    var progressNumber by remember { mutableStateOf(0.0f) }
    val animatedProgress = animateFloatAsState(
        targetValue = progressNumber,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
    ).value

    if (nav) {
        mainViewModel.setOnLineState()
        mainViewModel.getCurrentUserModel {
            if (it == 1){
                registerViewModel.goToMainScreen()
                mainViewModel.updateContacts(initContacts(mainActivity)) { result ->
                    if (result == 1) {

                    } else {
                    }//error
                }
            }
        }
    }
    if (code.length == 6) {
        coroutineScopeError.launch {
            while (true) {
                delay(10L)
                scale2 += 0.1f
                if (scale2 == 0.8000001f) {
                    break
                }
            }
            delay(2000L)
            while (true) {
                delay(2L)
                scale2 -= 0.01f
                if (scale2 == 5.5134296E-7f) {
                    scale2 = 0f
                    break
                }
            }
        }
        registerViewModel.checkForAuth {
            if (!it)
                registerViewModel.startAuthorisationByPhone(code) { state1 ->
                    when (state1) {
                        0 -> {
                            textError = "Введён неверный код"
                            CoroutineScope(Dispatchers.IO).launch {
                                visible3 = true
                                delay(2500L)
                                visible3 = false
                            }
                        }
                        1 -> {
                            CoroutineScope(Dispatchers.IO).launch {
                                while (true) {
                                    delay(5L)
                                    scale1 -= 0.01f
                                    if (scale1 == -0.009999329f) {
                                        coroutineScopeError.cancel()
                                        coroutineScope1.cancel()
                                        nav = true
                                        break
                                    }
                                }
                            }
                        }
                        2 -> {
                            textError = "Ошибка сети, авторизуйтесь заново"
                            CoroutineScope(Dispatchers.IO).launch {
                                visible3 = true
                                delay(1000L)
                                visible3 = false
                            }
                        }
                    }
                }
        }
    }
    progressNumber = when (phoneNumber.length) {
        0 -> 0.0f
        1 -> 0.083f
        2 -> 0.17f
        3 -> 0.249f
        4 -> 0.332f
        5 -> 0.415f
        6 -> 0.498f
        7 -> 0.581f
        8 -> 0.664f
        9 -> 0.747f
        10 -> 0.83f
        11 -> 0.913f
        12 -> 1f
        else -> 1f
    }
    colorBar = if (progressNumber == 1f)
        GreenBar
    else Red
    Column(
        verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.Start,
        modifier = modifier.background(if (!isSystemInDarkTheme()) Yellow900 else Black)

    ) {
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Tristagram",
                fontSize = 40.sp,
                style = H9,
                color = if (!isSystemInDarkTheme()) Navy700_1 else Navy500_1,
                modifier = Modifier.padding(top = 7.dp)
            )
        }

        Visibility(visible = visible) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 10.dp)
            ) {
                Box(
                    Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                ) {
                    CellOutlinedField(
                        value = phoneNumber,
                        text = stringResource(id = R.string.number)
                    ) {
                        phoneNumber = it
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    LinearProgressIndicator(progress = animatedProgress, color = colorBar)
                }
                CellTextAnnotation(stringResource(id = R.string.reg_message))
                Visibility(visible = visible4) {
                    Card(
                        border = BorderStroke(1.dp, Color.Red),
                        shape = CircleShape
                    ) {
                        CellTextError(textError)
                    }
                }
                Icon(
                    painter = painterResource(id = R.drawable.twotone_tty_24),
                    contentDescription = null,
                    modifier = Modifier
                        .size(
                            70.dp, 70.dp
                        )
                        .transformable(state = state)
                        .graphicsLayer(
                            rotationY = rotation
                        ),
                    tint = Navy700_1
                )
            }
        }
    }
    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Bottom,
        modifier = modifier
            .padding(bottom = 240.dp, end = 60.dp)
    ) {
        OutlinedButton(
            onClick = {
                if (phoneNumber.isNotEmpty())
                    if (phoneNumber.length == 12 && phoneNumber.substring(0, 1) == "+"
                        && phoneNumber.substring(1, 12).isDigitsOnly()
                    ) {
                        keyboardController?.hide()
                        enable = false
                        coroutineScope.launch {
                            rotation = 0f
                            while (true) {
                                delay(1L)
                                rotation += 0.7f
                                if (rotation == 0f) {
                                    break
                                }
                            }
                        }
                        registerViewModel.startRegistrationByPhone(
                            mainActivityComponent = mainActivity,
                            phoneNumber = phoneNumber
                        ) { state ->
                            when (state) {
                                0 -> {
                                    textError = " Авторизация не удалась, попробуйте позже"
                                    CoroutineScope(Dispatchers.IO).launch {
                                        visible4 = true
                                        delay(3000L)
                                        visible4 = false
                                        coroutineScope.cancel()
                                        rotation = 0f
                                        enable = true
                                    }
                                }
                                1 -> {
                                    CoroutineScope(Dispatchers.IO).launch {
                                        launch {
                                            while (true) {
                                                delay(10L)
                                                scale -= 0.01f
                                                if (scale == -0.009999329f) {
                                                    coroutineScope.cancel()
                                                    break
                                                }
                                            }
                                        }
                                        phoneNumber = ""
                                        visible = false
                                        delay(1000L)
                                        visible1 = true
                                        scale3 = 0.8f
                                        coroutineScope1.launch {
                                            while (true) {
                                                delay(5L)
                                                rotation2 += 2f
                                                rotation1 += 0.7f
                                                if (rotation == 0f) {
                                                    break
                                                }
                                            }
                                        }
                                    }
                                }
                                else -> {
                                    code = state.toString()
                                    CoroutineScope(Dispatchers.IO).launch {
                                        delay(1000L)
                                        CoroutineScope(Dispatchers.IO).launch {
                                            while (true) {
                                                delay(5L)
                                                scale1 -= 0.01f
                                                if (scale1 == -0.009999329f) {
                                                    scale2 = 0f
                                                    coroutineScopeError.cancel()
                                                    coroutineScope1.cancel()
                                                    nav = true
                                                    break
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        CoroutineScope(Dispatchers.IO).launch {
                            textError = "Неверный формат номера"
                            visible4 = true
                            delay(1000L)
                            visible4 = false
                        }
                    }
                else CoroutineScope(Dispatchers.IO).launch {
                    textError = "Введите номер"
                    visible4 = true
                    delay(1000L)
                    visible4 = false
                }
            },
            border = BorderStroke(1.dp, if (!isSystemInDarkTheme()) Navy700_1 else Navy500_1),
            modifier = Modifier
                .size(60.dp)
                .transformable(state = state)
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                ),
            shape = CircleShape,
            contentPadding = PaddingValues(0.dp),
            elevation = ButtonDefaults.elevation(
                defaultElevation = 20.dp,
                pressedElevation = 12.dp,
                disabledElevation = 5.dp
            ),
            enabled = enable,
        ) {
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null
            )
        }
    }
    Visibility(visible = visible1) {
        Column(
            modifier
                .padding(start = 30.dp, end = 30.dp, top = 30.dp, bottom = 200.dp)
                .transformable(state = state1)
                .graphicsLayer(
                    scaleX = scale1,
                    scaleY = scale1,
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.twotone_vpn_key_24),
                contentDescription = null,
                Modifier
                    .size(80.dp, 80.dp)
                    .transformable(state = state1)
                    .graphicsLayer(
                        scaleX = scale3,
                        scaleY = scale3,
                        rotationY = rotation1,
                        rotationX = rotation1,
                        rotationZ = rotation1,
                    ),
                tint = Navy700_1
            )

            Icon(
                Icons.Default.Update,
                contentDescription = null,
                Modifier
                    .size(80.dp, 80.dp)
                    .transformable(state = state1)
                    .graphicsLayer(
                        scaleX = scale2,
                        scaleY = scale2,
                        rotationZ = rotation2
                    ),
                tint = Navy700_1
            )

            CellOutlinedField(code, stringResource(id = R.string.enter_code)) {
                if (it.length <= 6)
                    code = it
            }
            Box() {
                CellTextAnnotation(text = stringResource(id = R.string.code_message))
            }
            Visibility(visible = visible3) {
                Card(
                    border = BorderStroke(1.dp, Color.Red), modifier = Modifier.padding(10.dp),
                    shape = CircleShape
                ) {
                    CellTextError(textError)
                }
            }
        }
    }
}


