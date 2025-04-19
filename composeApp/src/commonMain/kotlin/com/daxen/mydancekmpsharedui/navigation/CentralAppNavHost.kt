package com.daxen.mydancekmpsharedui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.daxen.mydancekmpsharedui.features.auth.PersonalInfoScreenRoute
import com.daxen.mydancekmpsharedui.features.auth.RegisterScreenRoute
import com.daxen.mydancekmpsharedui.features.auth.RoleSelectionScreenRoute
import com.daxen.mydancekmpsharedui.features.auth.authNavGraph
import com.daxen.mydancekmpsharedui.features.user.userOptionsNavGraph
import com.daxen.mydancekmpsharedui.main.mainNavGraph

@Composable
fun CentralAppNavHost(navController: NavHostController, isUserLogged: Boolean) {
    NavHost(
        navController = navController,
        startDestination = if (isUserLogged) CentralAppDestination.Main.route else CentralAppDestination.Auth.route
    ) {
        authNavGraph(
            goToUser = {
                navController.navigate(CentralAppDestination.Main.route) {
                    popUpTo(0) { inclusive = true}
                }
            },
            goToRegister = { navController.navigate(RegisterScreenRoute) },
            goToRoleSelection = {
                navController.navigate(RoleSelectionScreenRoute) {
                    popUpTo(0) { inclusive = true }
                }
            },
            goToInfo = { navController.navigate(PersonalInfoScreenRoute) },
            goBack = { navController.popBackStack() }
        )

        mainNavGraph(navController = navController)

        userOptionsNavGraph(appNavController = navController)
    }
}