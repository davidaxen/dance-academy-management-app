package com.daxen.mydancekmpsharedui.features.user.ui.academyUser

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.daxen.mydancekmpsharedui.core.ui.composables.ErrorComponent
import com.daxen.mydancekmpsharedui.core.ui.composables.LoadingComponent
import com.daxen.mydancekmpsharedui.features.user.AcademyProfileAction
import com.daxen.mydancekmpsharedui.features.user.ui.academyUser.components.MainAcademyUserSection

@Composable
internal fun AcademyUserScreen(
    viewModel: AcademyUserViewModel,
    navigateToSection: (AcademyProfileAction) -> Unit
) {
    val userState by viewModel.userState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (userState) {
            is AcademyUserUiState.Loading -> {
                LoadingComponent(text = "Cargando academia...")
            }

            is AcademyUserUiState.Success -> {
                MainAcademyUserSection(
                    navigateToSection = navigateToSection
                )
            }

            is AcademyUserUiState.Error -> {
                ErrorComponent(
                    message = "Error al cargar la academia",
                    onRetry = {
                        viewModel.reloadUser()
                    }
                )
            }
        }
    }
} 