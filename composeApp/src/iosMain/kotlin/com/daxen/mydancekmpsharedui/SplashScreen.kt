package com.daxen.mydancekmpsharedui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.toComposeImageBitmap
import platform.UIKit.UIImage

@Composable
fun IosSplashScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = SplashLogoPainter(),
            contentDescription = null,
            modifier = Modifier.size(150.dp)
        )
    }
}

@Composable
fun SplashLogoPainter(): Painter {
    val imageName = "splash_logo"
    val uiImage = UIImage.imageNamed(imageName)!!
    return remember { BitmapPainter(uiImage.toComposeImageBitmap()) }
}