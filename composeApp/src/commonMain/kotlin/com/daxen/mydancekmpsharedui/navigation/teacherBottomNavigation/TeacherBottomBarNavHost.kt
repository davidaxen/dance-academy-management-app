package com.daxen.mydancekmpsharedui.navigation.teacherBottomNavigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.daxen.mydancekmpsharedui.features.teacher.classes.teacherClassesGraph
import com.daxen.mydancekmpsharedui.features.user.userNavGraph
import com.daxen.mydancekmpsharedui.navigation.CentralAppDestination

@Composable
fun TeacherBottomBarNavHost(bottomNavController: NavHostController, appNavController: NavHostController) {
    NavHost(
        navController = bottomNavController,
        startDestination = TeacherBottomBarDestination.ClassesList.route
    ) {
        userNavGraph(
            appNavController = appNavController,
            navigateToLogin = {
                appNavController.navigate(CentralAppDestination.Auth.route)
            }
        )

        teacherClassesGraph(
            appNavController = appNavController
        )

//        reservationNavGraph()
//
//        calendarNavGraph()
    }
}