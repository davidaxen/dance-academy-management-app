package com.daxen.mydancekmpsharedui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.daxen.mydancekmpsharedui.features.auth.authNavGraph
import com.daxen.mydancekmpsharedui.features.user.userNavGraph
import com.daxen.mydancekmpsharedui.navigation.bottomnavigation.BottomDestinations

@Composable
fun CentralNavigation(navController: NavHostController, isUserLogged: Boolean) {
    NavHost(
        navController = navController,
        startDestination = if (isUserLogged) BottomDestinations.User.route else BottomDestinations.Auth.route
    ) {
        userNavGraph()
        authNavGraph(goToUser = {
            navController.navigate(BottomDestinations.User.route)
        })
    }
}