package com.daxen.mydancekmpsharedui.features.auth

import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.daxen.mydancekmpsharedui.data.user.model.UserRole
import com.daxen.mydancekmpsharedui.features.auth.ui.personal_info.PersonalInfoScreen
import com.daxen.mydancekmpsharedui.features.auth.ui.login.LoginScreen
import com.daxen.mydancekmpsharedui.features.auth.ui.login.LoginViewModel
import com.daxen.mydancekmpsharedui.features.auth.ui.register.RegisterScreen
import com.daxen.mydancekmpsharedui.features.auth.ui.register.RegisterViewModel
import com.daxen.mydancekmpsharedui.features.auth.ui.role.RoleSelectionScreen
import com.daxen.mydancekmpsharedui.features.auth.ui.role_selection.DanceRoleSelectionScreen
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

@Serializable
data object PersonalInfoScreenRoute

@Serializable
data object DanceRoleSelectionScreenRoute

fun NavGraphBuilder.authNavGraph(
    goToUser: () -> Unit,
    goToRegister: () -> Unit,
    goToRoleSelection: () -> Unit,
    goToInfo: () -> Unit,
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

        composable<RoleSelectionScreenRoute>(
            enterTransition = { fadeIn(animationSpec = tween(500, easing = EaseIn)) },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(500)
                )
            },
        ) {
            RoleSelectionScreen(
                onRoleSelected = { role ->
                    when (role) {
                        UserRole.STUDENT -> { goToInfo() }
                        UserRole.TEACHER -> {}
                        UserRole.ACADEMY -> {}
                    }
                },
                onLogOut = goBack,
                modifier = Modifier.fillMaxSize(),
            )
        }

        composable<PersonalInfoScreenRoute>(
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(500)
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(500)
                )
            },
        ) {
            PersonalInfoScreen(
                onNavigateBack = goBack,
                onNavigateNext = goToUser,
                modifier = Modifier.fillMaxSize()
            )
        }

        composable<DanceRoleSelectionScreenRoute>(
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(500)
                )
            },
        ) {
            DanceRoleSelectionScreen(
                onNavigateBack = goBack,
                onNavigateNext = goToRoleSelection,
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
                slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(1000, easing = EaseIn)
                )
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