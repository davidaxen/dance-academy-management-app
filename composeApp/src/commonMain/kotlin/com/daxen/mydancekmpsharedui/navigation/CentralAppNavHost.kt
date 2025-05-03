package com.daxen.mydancekmpsharedui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.daxen.mydancekmpsharedui.features.auth.AcademyRegisterProcessNavGraph
import com.daxen.mydancekmpsharedui.features.auth.DanceRoleSelectionScreenRoute
import com.daxen.mydancekmpsharedui.features.auth.LoginScreenRoute
import com.daxen.mydancekmpsharedui.features.auth.PersonalInfoScreenRoute
import com.daxen.mydancekmpsharedui.features.auth.PostSplashDestination
import com.daxen.mydancekmpsharedui.features.auth.RegisterProcessNavGraph
import com.daxen.mydancekmpsharedui.features.auth.RegisterScreenRoute
import com.daxen.mydancekmpsharedui.features.auth.academyRegisterProcessNavGraph
import com.daxen.mydancekmpsharedui.features.auth.authNavGraph
import com.daxen.mydancekmpsharedui.features.auth.registerProcessNavGraph
import com.daxen.mydancekmpsharedui.features.user.AcademySelectionGraph
import com.daxen.mydancekmpsharedui.features.user.academySelectionNavGraph
import com.daxen.mydancekmpsharedui.features.user.userOptionsNavGraph
import com.daxen.mydancekmpsharedui.main.mainNavGraph

@Composable
fun CentralAppNavHost(
    navController: NavHostController,
    destination: PostSplashDestination
) {
    @Suppress("IMPLICIT_CAST_TO_ANY")
    val startDestination = when (destination) {
        PostSplashDestination.Login -> CentralAppDestination.Auth.route
        PostSplashDestination.AcademySelection -> AcademySelectionGraph
        PostSplashDestination.CompleteProfile -> RegisterProcessNavGraph
    }
    NavHost(
        navController = navController,
//        startDestination = startDestination
        startDestination = AcademyRegisterProcessNavGraph
    ) {
        authNavGraph(
            goToAcademySelection = {
                navController.navigate(AcademySelectionGraph) {
                    popUpTo(0) { inclusive = true}
                }
            },
            goToRegister = { navController.navigate(RegisterScreenRoute) },
            goToRoleSelection = {
                navController.navigate(RegisterProcessNavGraph) {
                    popUpTo(0) { inclusive = true }
                }
            },
           goBack = { navController.popBackStack() }
        )

        academyRegisterProcessNavGraph(

        )

        registerProcessNavGraph(
            goToLogin = { navController.navigate(LoginScreenRoute) },
            goToInfo = { navController.navigate(PersonalInfoScreenRoute) },
            goToDanceRoleSelection = { navController.navigate(DanceRoleSelectionScreenRoute) },
            goToAcademySelection = {
                navController.navigate(AcademySelectionGraph) {
                    popUpTo(0) { inclusive = true}
                }
            },
            goBack = { navController.popBackStack() }
        )

        mainNavGraph(navController = navController)

        academySelectionNavGraph(
            appNavController = navController,
            navigateToLogin = { navController.navigate(LoginScreenRoute) }
        )

        userOptionsNavGraph(appNavController = navController)
    }
}