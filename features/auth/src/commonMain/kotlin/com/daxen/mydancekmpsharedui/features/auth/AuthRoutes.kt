package com.daxen.mydancekmpsharedui.features.auth

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.daxen.mydancekmpsharedui.features.auth.ui.login.LoginScreen
import com.daxen.mydancekmpsharedui.features.auth.ui.login.LoginViewModel
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel

@Serializable
data object Auth

fun NavGraphBuilder.authRoutes(
//    goToExample: () -> Unit,
) {
    composable<Auth> {
        val viewModel: LoginViewModel = koinViewModel()
        LoginScreen(
            viewModel = viewModel,
            modifier = Modifier.fillMaxSize()
        )
    }
}