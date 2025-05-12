package com.daxen.mydancekmpsharedui.features.user.ui.academyUser.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.daxen.mydancekmpsharedui.data.user.model.UserAcademy
import com.daxen.mydancekmpsharedui.features.user.ProfileAction
import com.daxen.mydancekmpsharedui.features.user.ui.academyUser.utils.AcademyConstants
import com.daxen.mydancekmpsharedui.features.user.ui.components.ProfileSectionCard
import com.daxen.mydancekmpsharedui.features.user.utils.ProfileItem

@Composable
internal fun MainAcademyUserSection(
    user: UserAcademy,
    showTopSection: Boolean = false,
    navigateToLogin: () -> Unit,
    navigateToSection: (ProfileAction) -> Unit
) {
    val dialogState = remember { mutableStateOf(false) }
    Column(modifier = Modifier.fillMaxSize()) {
        // Header con icono de usuario y nombre (opcional)
        if (showTopSection) {
            AcademyUserDataSummary(user) {
                navigateToLogin()
            }
        }

        // Lista de opciones
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(bottom = 8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            AcademyCardsSection(navigateToSection)

            Spacer(modifier = Modifier.weight(1f))

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth().padding(top = 64.dp)
            ) {
                if (dialogState.value) {
                    Dialog(onDismissRequest = { dialogState.value = false }) {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            )
                        ) {
                            Text("Probando dialogo", modifier = Modifier.padding(24.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AcademyCardsSection(navigateToSection: (ProfileAction) -> Unit) {
    var currentSectionTitle: String? = null
    val currentOptions = mutableListOf<ProfileItem.Option>()

    AcademyConstants.profileItems.forEachIndexed { index, item ->
        when (item) {
            is ProfileItem.Section -> {
                if (currentSectionTitle != null && currentOptions.isNotEmpty()) {
                    ProfileSectionCard(
                        title = currentSectionTitle!!,
                        options = currentOptions,
                        navigateToSection = navigateToSection,
                    )
                    currentOptions.clear()
                }
                currentSectionTitle = item.title
            }

            is ProfileItem.Option -> {
                currentOptions.add(item)
                if (index == AcademyConstants.profileItems.lastIndex) {
                    ProfileSectionCard(
                        title = currentSectionTitle ?: "",
                        options = currentOptions,
                        navigateToSection = navigateToSection,
                    )
                }
            }
        }
    }
} 