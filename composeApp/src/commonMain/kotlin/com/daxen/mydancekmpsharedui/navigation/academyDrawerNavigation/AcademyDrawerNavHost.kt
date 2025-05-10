package com.daxen.mydancekmpsharedui.navigation.academyDrawerNavigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import com.daxen.mydancekmpsharedui.features.academy.students.academyStudentsGraph
import com.daxen.mydancekmpsharedui.features.user.userNavGraph
import com.daxen.mydancekmpsharedui.navigation.CentralAppDestination

@Composable
fun AcademyDrawerNavHost(drawerNavController: NavHostController, appNavController: NavHostController) {
    NavHost(
        navController = drawerNavController,
        startDestination = AcademyDrawerDestination.User.route
    ) {
        userNavGraph(
            appNavController = appNavController,
            navigateToLogin = {
                appNavController.navigate(CentralAppDestination.Auth.route)
            },
        )
        academyStudentsGraph()
//        teachersListNavGraph()
    }
}