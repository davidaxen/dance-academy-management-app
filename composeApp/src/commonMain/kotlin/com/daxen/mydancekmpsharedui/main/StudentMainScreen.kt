package com.daxen.mydancekmpsharedui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.daxen.mydancekmpsharedui.navigation.studentBottomNavigation.StudentBottomBarNavigation
import com.daxen.mydancekmpsharedui.navigation.studentBottomNavigation.StudentBottomBarNavHost

@Composable
fun StudentMainScreen(appNavController: NavHostController) {
    val bottomNavController = rememberNavController()

    Scaffold (
        bottomBar = {
            StudentBottomBarNavigation(bottomNavController)
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            StudentBottomBarNavHost(bottomNavController = bottomNavController, appNavController = appNavController)
        }
    }
}