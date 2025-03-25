package com.daxen.mydancekmpsharedui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.daxen.mydancekmpsharedui.features.auth.authNavGraph
import com.daxen.mydancekmpsharedui.features.user.userOptionsNavGraph
import com.daxen.mydancekmpsharedui.main.mainNavGraph

@Composable
fun CentralAppNavHost(navController: NavHostController, isUserLogged: Boolean) {
    NavHost(
        navController = navController,
        startDestination = if (isUserLogged) CentralAppDestination.Main.route else CentralAppDestination.Auth.route
    ) {
        authNavGraph(goToUser = {
            navController.navigate(CentralAppDestination.Main.route) {
                popUpTo(0) {
                    inclusive = true
                }
            }

        })

        mainNavGraph(
            navController = navController
        )

        userOptionsNavGraph(appNavController = navController)
    }
}