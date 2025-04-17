package com.daxen.mydancekmpsharedui.features.auth

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.daxen.mydancekmpsharedui.features.auth.ui.login.LoginScreen
import com.daxen.mydancekmpsharedui.features.auth.ui.login.LoginViewModel
import com.daxen.mydancekmpsharedui.features.auth.ui.register.RegisterScreen
import com.daxen.mydancekmpsharedui.features.auth.ui.register.RegisterViewModel
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel

@Serializable
data object AuthGraph

@Serializable
data object LoginScreenRoute

@Serializable
data object RegisterScreenRoute

fun NavGraphBuilder.authNavGraph(
    goToUser: () -> Unit,
    goToRegister: () -> Unit,
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
        
        composable<RegisterScreenRoute> {
            val viewModel: RegisterViewModel = koinViewModel()
            RegisterScreen(
                viewModel = viewModel,
                navigateToLogin = goBack,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}