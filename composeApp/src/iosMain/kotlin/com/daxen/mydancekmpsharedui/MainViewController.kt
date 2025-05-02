package com.daxen.mydancekmpsharedui

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.window.ComposeUIViewController
import com.daxen.mydancekmpsharedui.features.auth.AuthViewModel
import org.koin.compose.getKoin

fun MainViewController() = ComposeUIViewController {
    val authViewModel: AuthViewModel = getKoin().get()
    val destination by authViewModel.destination.collectAsState()
    when (destination) {
        null -> IosSplashScreen()
        else -> App(destination = destination!!)
    }
}