package com.daxen.mydancekmpsharedui.features.user.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.daxen.mydancekmpsharedui.features.user.ProfileAction
import com.daxen.mydancekmpsharedui.features.user.ui.components.MainUserSection

@Composable
internal fun UserScreen(
    viewModel: UserViewModel,
    showTopSection: Boolean = false,
    navigateToLogin: () -> Unit,
    navigateToSection: (ProfileAction) -> Unit
) {
    val currentUser by viewModel.currentUser.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        MainUserSection(
            currentUser,
            showTopSection = showTopSection,
            navigateToLogin = navigateToLogin,
            navigateToSection = navigateToSection
        )
    }
}
