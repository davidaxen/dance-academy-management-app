package com.daxen.mydancekmpsharedui.features.user.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.daxen.mydancekmpsharedui.data.user.model.User
import com.daxen.mydancekmpsharedui.features.user.ProfileAction
import com.daxen.mydancekmpsharedui.features.user.utils.Constants
import com.daxen.mydancekmpsharedui.features.user.utils.ProfileItem

@Composable
internal fun MainUserSection(
    user: User,
    navigateToSection: (ProfileAction) -> Unit
) {
    val dialogState = remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Header con icono de usuario y nombre (opcional)
        Surface(
            elevation = 4.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            UserDataSummary(user)
        }

        // Lista de opciones
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(bottom = 8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            CardsSection(navigateToSection)

            Spacer(modifier = Modifier.weight(1f))

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth().padding(top = 64.dp)
            ) {
                TextButton({
                    dialogState.value = true
                }, modifier = Modifier.padding(bottom = 2.dp)) {
                    Text("BORRAR CUENTA", color = Color.Red)
                }
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
private fun CardsSection(navigateToSection: (ProfileAction) -> Unit) {
    var currentSectionTitle: String? = null
    val currentOptions = mutableListOf<ProfileItem.Option>()

    Constants.profileItems.forEachIndexed { index, item ->
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
                if (index == Constants.profileItems.lastIndex) {
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

@Composable
private fun UserDataSummary(user: User) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(vertical = 24.dp, horizontal = 16.dp)
            .fillMaxWidth()
    ) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "User Icon",
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .border(2.dp, Color.Gray, CircleShape)
                .padding(16.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Text(
                text = user.name,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = user.email,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}