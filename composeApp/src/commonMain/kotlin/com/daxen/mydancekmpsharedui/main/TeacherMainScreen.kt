package com.daxen.mydancekmpsharedui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.daxen.mydancekmpsharedui.navigation.teacherBottomNavigation.TeacherBottomBarNavHost
import com.daxen.mydancekmpsharedui.navigation.teacherBottomNavigation.TeacherBottomBarNavigation

@Composable
fun TeacherMainScreen(appNavController: NavHostController) {
    val bottomNavController = rememberNavController()

    Scaffold (
        bottomBar = {
            TeacherBottomBarNavigation(bottomNavController)
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            TeacherBottomBarNavHost(bottomNavController = bottomNavController, appNavController = appNavController)
        }
    }
}