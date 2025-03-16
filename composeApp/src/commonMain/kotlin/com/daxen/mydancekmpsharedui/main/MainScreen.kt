package com.daxen.mydancekmpsharedui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.daxen.mydancekmpsharedui.navigation.bottomnavigation.AppBottomNavigation
import com.daxen.mydancekmpsharedui.navigation.bottomnavigation.BottomBarNavHost

@Composable
fun MainScreen(appNavController: NavHostController) {
    val bottomNavController = rememberNavController()

    Scaffold (
        bottomBar = {
            AppBottomNavigation(bottomNavController)
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {

            BottomBarNavHost(bottomNavController = bottomNavController, appNavController = appNavController)
        }
    }
}