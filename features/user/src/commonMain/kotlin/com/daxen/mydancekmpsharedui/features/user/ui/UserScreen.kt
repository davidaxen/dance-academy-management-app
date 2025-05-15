package com.daxen.mydancekmpsharedui.features.user.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.daxen.mydancekmpsharedui.core.ui.composables.ErrorComponent
import com.daxen.mydancekmpsharedui.core.ui.composables.LoadingComponent
import com.daxen.mydancekmpsharedui.features.user.ProfileAction
import com.daxen.mydancekmpsharedui.features.user.ui.components.MainUserSection

@Composable
internal fun UserScreen(
    viewModel: UserViewModel,
    showTopSection: Boolean = false,
    navigateToLogin: () -> Unit,
    navigateToSection: (ProfileAction) -> Unit
) {
    val userState by viewModel.userState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.reloadUser()
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (userState) {
            is UserUiState.Loading -> {
                LoadingComponent(text = "Cargando usuario...")
            }

            is UserUiState.Success -> {
                val user = (userState as UserUiState.Success).user
                MainUserSection(
                    user,
                    showTopSection = showTopSection,
                    navigateToLogin = navigateToLogin,
                    navigateToSection = navigateToSection
                )
            }

            is UserUiState.Error -> {
                ErrorComponent(
                    message = "Error al cargar el usuario",
                    onRetry = {
                        viewModel.reloadUser()
                    }
                )
            }
        }
    }
}
