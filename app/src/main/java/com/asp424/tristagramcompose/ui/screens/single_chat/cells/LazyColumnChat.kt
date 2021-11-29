package com.asp424.tristagramcompose.ui.screens.single_chat.cells

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.asp424.tristagramcompose.data.repos.single_chat.MessageResponse
import com.asp424.tristagramcompose.data.room.single_chat.MessageModelRoom
import com.asp424.tristagramcompose.utils.USER
import com.asp424.tristagramcompose.utils.log
import com.asp424.tristagramcompose.viewmodels.SingleChatViewModel
import dev.chrisbanes.snapper.rememberLazyListSnapperLayoutInfo
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class, dev.chrisbanes.snapper.ExperimentalSnapperApi::class)
@SuppressLint("CoroutineCreationDuringComposition", "UnrememberedMutableState")
@Composable
fun LazyColumnChat(
    list: SnapshotStateList<MessageModelRoom>,
    singleChatViewModel: SingleChatViewModel,
    userName: String?,
    onSwipeLeft: () -> Unit
) {
    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = list.lastIndex
    )


    LaunchedEffect(list) { listState.scrollToItem(list.lastIndex) }
    val height by singleChatViewModel.height.observeAsState(0f)
    var max by remember { mutableStateOf(height) }
    var min by remember { mutableStateOf(height) }
    val localDens = LocalDensity.current.density
    val focus = LocalFocusManager.current
    val stateKeyboard by singleChatViewModel.keyBoardState.observeAsState(false)
    if (stateKeyboard) max = height else min = height
    val ass by animateDpAsState(if (stateKeyboard) max.dp else min.dp)
    // LaunchedEffect(listState.value) {
    //  if (listState.value in 0..9) {
    //       countMessages += 10
    //       singleChatViewModel.getMessages(countMessages = countMessages) {

    //       }
    //        listState.animateScrollTo(listState.value + 10)
    //    }
    // }
    LaunchedEffect(stateKeyboard) {
        if (!stateKeyboard) focus.clearFocus()
        listState.animateScrollBy(
            if (stateKeyboard) { (max - min) * localDens
            } else { -(max - min) * localDens
            }
        )
    }

    LazyColumn(
        content = {
            itemsIndexed(list) { index, item ->
                if (userName != null) {
                    item.from?.let {
                        TextMessageBlock(item, singleChatViewModel, userName, it,
                            visibleChange = {})
                    }
                }
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 14.dp, end = 14.dp),
        verticalArrangement = Arrangement.Bottom,
        state = listState, contentPadding = PaddingValues(bottom = ass)
    )
}