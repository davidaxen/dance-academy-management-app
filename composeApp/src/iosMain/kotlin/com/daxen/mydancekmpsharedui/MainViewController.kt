package com.daxen.mydancekmpsharedui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.window.ComposeUIViewController
import com.daxen.mydancekmpsharedui.features.auth.AuthViewModel
import org.koin.compose.getKoin
import org.koin.core.context.startKoin
import org.koin.mp.KoinPlatformTools

fun MainViewController() = ComposeUIViewController {
    if (KoinPlatformTools.defaultContext().getOrNull() == null) {
        startKoin {
            modules(appModule)
        }
    }
    val authViewModel: AuthViewModel = getKoin().get()
    authViewModel.checkStartingUserState()

    ShowScreen(authViewModel)
}

@Composable
fun ShowScreen(viewModel: AuthViewModel) {
    val destination by viewModel.startingDestination.collectAsState()
    when (destination) {
        null -> IosSplashScreen()
        else -> App(destination = destination!!)
    }
}