package com.daxen.mydancekmpsharedui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.daxen.mydancekmpsharedui.navigation.bottomnavigation.AppBottomNavigation
import com.daxen.mydancekmpsharedui.navigation.CentralNavigation
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
            val navController = rememberNavController()

            val isUserLogged = Firebase.auth.currentUser != null
            Scaffold (
                bottomBar = {
                     if (isUserLogged) AppBottomNavigation(navController)
                }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                ) {

                    CentralNavigation(navController, isUserLogged)
                }
            }

//            CoroutineScope(Dispatchers.Main).launch {
//                //Firebase.auth.signOut()
//            }

//            if (Firebase.auth.currentUser != null) {
//
//            } else {
//                NavigationWrapper()
//            }

        }
    }
}