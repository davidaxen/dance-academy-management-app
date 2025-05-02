package com.daxen.mydancekmpsharedui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.daxen.mydancekmpsharedui.core.ui.theme.MyDanceTheme
import com.daxen.mydancekmpsharedui.features.auth.PostSplashDestination
import com.daxen.mydancekmpsharedui.navigation.CentralAppNavHost
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(destination: PostSplashDestination) {
    MyDanceTheme {
        val navController = rememberNavController()
        CentralAppNavHost(navController, destination)
    }
}