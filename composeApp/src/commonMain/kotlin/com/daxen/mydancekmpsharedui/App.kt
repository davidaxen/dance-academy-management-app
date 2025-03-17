package com.daxen.mydancekmpsharedui

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.daxen.mydancekmpsharedui.navigation.CentralAppNavHost
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import org.koin.core.module.Module

@Composable
@Preview
fun App(
    platformModule: Module = Module()
) {
    KoinApplication(
        application = {
            modules(appModule, platformModule)
        }
    ) {
        MaterialTheme {
            val isUserLogged = Firebase.auth.currentUser != null
            val navController = rememberNavController()
            CentralAppNavHost(navController,isUserLogged)
        }
    }
}