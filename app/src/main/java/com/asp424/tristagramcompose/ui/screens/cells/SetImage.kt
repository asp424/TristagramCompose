package com.asp424.tristagramcompose.ui.screens.cells

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.compose.foundation.ExperimentalFoundationApi

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.*

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

import com.asp424.tristagramcompose.MainActivity
import com.asp424.tristagramcompose.R
import com.asp424.tristagramcompose.utils.log
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.accompanist.glide.rememberGlidePainter
import kotlinx.coroutines.launch



@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SetImage(
    photoUrl: String,
    onClick1: () -> Unit) {
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    Glide.with(LocalContext.current).asBitmap()
        .load(photoUrl)
        .into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                bitmap = resource
            }
            override fun onLoadCleared(placeholder: Drawable?) { R.drawable.logo }
        })
    if (bitmap != null)
        Image(bitmap = bitmap!!.asImageBitmap(), modifier = Modifier
            .size(54.dp)
            .clip(CircleShape)
            .clickable { onClick1() }, contentDescription = null)
}



