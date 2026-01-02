package com.daxen.mydancekmpsharedui.main

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import kotlinx.serialization.Serializable

@Serializable
data object StudentMainGraph
@Serializable
data object StudentMainScreen

@Serializable
data object TeacherMainGraph
@Serializable
data object TeacherMainScreen

@Serializable
data object AcademyMainGraph
@Serializable
data object AcademyMainScreen

fun NavGraphBuilder.mainNavGraph(
    navController: NavHostController,
) {
    navigation<StudentMainGraph>(startDestination = StudentMainScreen) {
        composable<StudentMainScreen> {
            StudentMainScreen(appNavController = navController)
        }
    }

    navigation<TeacherMainGraph>(startDestination = TeacherMainScreen) {
        composable<TeacherMainScreen> {
            TeacherMainScreen(appNavController = navController)
        }
    }

    navigation<AcademyMainGraph>(startDestination = AcademyMainScreen) {
        composable<AcademyMainScreen> {
            AcademyMainScreen(appNavController = navController)
        }
    }
}