package com.daxen.mydancekmpsharedui

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.daxen.mydancekmpsharedui.features.auth.Auth
import com.daxen.mydancekmpsharedui.features.auth.authRoutes
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
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = Auth
            ) {
                authRoutes(
//                    goToExample = {}

                )
            }
        }
    }
}