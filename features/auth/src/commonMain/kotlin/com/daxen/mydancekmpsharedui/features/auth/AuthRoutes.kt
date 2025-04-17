package com.daxen.mydancekmpsharedui.features.auth

import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.daxen.mydancekmpsharedui.data.user.model.UserRole
import com.daxen.mydancekmpsharedui.features.auth.ui.login.LoginScreen
import com.daxen.mydancekmpsharedui.features.auth.ui.login.LoginViewModel
import com.daxen.mydancekmpsharedui.features.auth.ui.register.RegisterScreen
import com.daxen.mydancekmpsharedui.features.auth.ui.register.RegisterViewModel
import com.daxen.mydancekmpsharedui.features.auth.ui.role.RoleSelectionScreen
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel

@Serializable
data object AuthGraph

@Serializable
data object LoginScreenRoute

@Serializable
data object RegisterScreenRoute

@Serializable
data object RoleSelectionScreenRoute

fun NavGraphBuilder.authNavGraph(
    goToUser: () -> Unit,
    goToRegister: () -> Unit,
    goToRoleSelection: () -> Unit,
    goBack: () -> Unit,
) {
    navigation<AuthGraph>(startDestination = LoginScreenRoute) {
        composable<LoginScreenRoute> {
            val viewModel: LoginViewModel = koinViewModel()
            LoginScreen(
                viewModel = viewModel,
                navigateToUserScreen = goToUser,
                navigateToRegister = goToRegister,
                modifier = Modifier.fillMaxSize()
            )
        }

        composable<RoleSelectionScreenRoute> {
            RoleSelectionScreen(
                onRoleSelected = { role ->
                    when (role) {
                        UserRole.STUDENT -> {}
                        UserRole.TEACHER -> {}
                        UserRole.ACADEMY -> {}
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }
        
        composable<RegisterScreenRoute>(
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(500)
                ) + fadeIn(animationSpec = tween(500, easing = LinearEasing))
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(500)
                ) + fadeOut(animationSpec = tween(500, easing = EaseIn))
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(500)
                ) + fadeIn(animationSpec = tween(500, easing = LinearEasing))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(500)
                ) + fadeOut(animationSpec = tween(500, easing = EaseIn))
            }
        ) {
            val viewModel: RegisterViewModel = koinViewModel()
            RegisterScreen(
                viewModel = viewModel,
                navigateToLogin = goBack,
                navigateToRoleSelection = goToRoleSelection,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}