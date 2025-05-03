package com.daxen.mydancekmpsharedui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitView
import platform.Foundation.NSBundle
import platform.UIKit.UIImage
import platform.UIKit.UIImageView
import platform.UIKit.UIViewContentMode

@Composable
fun IosSplashScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        SplashLogo()
    }
}

@Composable
fun SplashLogo() {
    UIKitView(factory = {
        val imageView = UIImageView()
        imageView.image = UIImage.imageNamed("splash_logo", inBundle = NSBundle.mainBundle, withConfiguration = null)
        imageView.contentMode = UIViewContentMode.UIViewContentModeScaleAspectFit
        imageView
    })
}