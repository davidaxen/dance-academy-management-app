package com.daxen.mydancekmpsharedui.main

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import kotlinx.serialization.Serializable

@Serializable
data object MainGraph

@Serializable
data object MainScreen

fun NavGraphBuilder.mainNavGraph(
    navController: NavHostController
) {
    navigation<MainGraph>(startDestination = MainScreen) {
        composable<MainScreen> {
            MainScreen(appNavController = navController)
        }
    }
}