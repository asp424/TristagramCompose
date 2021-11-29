package com.asp424.tristagramcompose.ui.screens.cells

import android.graphics.Rect
import android.location.LocationListener
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asp424.tristagramcompose.R
import com.asp424.tristagramcompose.data.repos.single_chat.UserInfoResponse
import com.asp424.tristagramcompose.ui.screens.contacts.cells.NameAndStatusBlockContacts
import com.asp424.tristagramcompose.ui.screens.single_chat.cells.SendBlock
import com.asp424.tristagramcompose.ui.theme.H9
import com.asp424.tristagramcompose.ui.theme.Navy500_1
import com.asp424.tristagramcompose.ui.theme.Navy700_1
import com.asp424.tristagramcompose.ui.theme.Yellow900
import com.asp424.tristagramcompose.viewmodels.SingleChatViewModel


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BaseScreen(
    singleChatViewModel: SingleChatViewModel? = null,
    headerText: String,
    verticalArrangement: Arrangement.Vertical,
    enableBack: Boolean = true, enableSettings: Boolean = false, enableHeader: Boolean = true,
    onBackPressedEnable: Boolean = true,
    inRow: @Composable (RowScope) -> Unit, inColumn: @Composable (ColumnScope) -> Unit,
    onBackPressed: () -> Unit, onSettingIconPressed: () -> Unit,
    userName: (String, String, String) -> Unit
) {
    Surface(
        Modifier
            .fillMaxSize(), color = if (!isSystemInDarkTheme()) Yellow900 else Color.Black
    ) {
        Row(
            horizontalArrangement = if (enableSettings) Arrangement.SpaceBetween else Arrangement.Start,
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp)
                .fillMaxWidth()
                .height(70.dp)
        ) {
            if (enableBack)
                BackIcon {
                    onBackPressed()
                }
            if (enableHeader)
                Text(
                    text = headerText,
                    fontSize = 40.sp,
                    style = H9,
                    color = if (!isSystemInDarkTheme()) Navy700_1 else Navy500_1,
                    modifier = Modifier.padding(
                        top = 6.dp, start = 20.dp
                    )
                )
            else {
                val userInfo by remember(singleChatViewModel) {
                    singleChatViewModel?.userInfo
                }!!.collectAsState()
                when (userInfo) {
                    is UserInfoResponse.OnSuccess -> {
                        val user = (userInfo as UserInfoResponse.OnSuccess).userInfo
                        userName(user.fullname!!, user.token!!, user.photoUrl!!)
                        Row(Modifier.padding(start = 14.dp)) {
                            user.apply {
                                ChangePhoto(
                                    fullName = fullname!!,
                                    photoUrl = photoUrl!!,
                                    color = color!!,
                                    onClick = { }, id = id) {
                                    NameAndStatusBlockContacts(fullName = fullname!!, state = state!!)
                                }
                            }
                        }
                    }
                    is UserInfoResponse.OnError -> {
                    }
                    else -> {
                    }
                }
            }
            if (enableSettings)
                Icon(
                    painter = painterResource(id = R.drawable.outline_settings_black_24dp),
                    contentDescription = null,
                    Modifier
                        .clickable { onSettingIconPressed() }
                        .padding(top = 23.dp)
                        .size(25.dp),
                    tint = if (!isSystemInDarkTheme()) Navy700_1 else Navy500_1
                )
            inRow(this)
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = verticalArrangement,
            modifier = Modifier
                .padding(top = 70.dp)
                .fillMaxSize(),
        ) {
            inColumn(this)
        }
    }

    if (onBackPressedEnable)
        BackHandler {
            onBackPressed()
        }
}