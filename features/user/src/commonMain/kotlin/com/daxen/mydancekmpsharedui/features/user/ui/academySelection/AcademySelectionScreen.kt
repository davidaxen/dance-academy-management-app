package com.daxen.mydancekmpsharedui.features.user.ui.academySelection

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.daxen.mydancekmpsharedui.features.user.ui.academySelection.components.AcademyCard
import com.daxen.mydancekmpsharedui.features.user.ui.academySelection.components.InvitationCard

@Composable
fun AcademySelectionScreen(
    viewModel: AcademySelectionViewModel,
) {
    val tabs = remember {
        listOf("Academias", "Invitaciones", "Perfil")
    }
    val tabIcons = remember {
        listOf(Icons.Default.Home, Icons.Default.Email, Icons.Default.AccountCircle)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TabRow(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            selectedTabIndex = viewModel.selectedTab,
            indicator = { tabPositions ->
                SecondaryIndicator(
                    modifier = Modifier
                        .tabIndicatorOffset(tabPositions[viewModel.selectedTab]),
                    height = 2.dp,
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    icon = { Icon(tabIcons[index], contentDescription = title) },
                    selected = viewModel.selectedTab == index,
                    onClick = { viewModel.onTabSelected(index) },
                    selectedContentColor = if (viewModel.selectedTab == index)
                        MaterialTheme.colorScheme.onBackground
                    else
                        MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.background(
                        if (viewModel.selectedTab == index)
                            MaterialTheme.colorScheme.background
                        else
                            MaterialTheme.colorScheme.primary
                    ),
                )
            }
        }

        when (viewModel.selectedTab) {
            0 -> {
                // Lista de academias
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(listOf(
                        Triple("Academia de Salsa", "Calle Mayor 123, Madrid", "https://rickandmortyapi.com/api/character/avatar/813.jpeg"),
                        Triple("Dance Studio", "Avenida Libertad 45, Barcelona", "https://rickandmortyapi.com/api/character/avatar/23.jpeg"),
                        Triple("Ritmo y Baile", "Plaza Central 7, Valencia", "https://rickandmortyapi.com/api/character/avatar/43.jpeg")
                    )) { (name, location, imageUrl) ->
                        AcademyCard(
                            name = name,
                            location = location,
                            imageUrl = imageUrl,
                            rating = 4.5f,
                            classCount = 12,
                            onClick = { /* TODO: Navegar a la academia */ }
                        )
                    }
                }
            }
            1 -> {
                // Lista de invitaciones
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(listOf("Academia 1", "Academia 2")) { academy ->
                        InvitationCard(
                            academyName = academy,
                            onAccept = { /* TODO: Aceptar invitación */ },
                            onReject = { /* TODO: Rechazar invitación */ }
                        )
                    }
                }
            }
            2 -> {
                // Perfil (por ahora vacío)
                Text(
                    text = "Perfil del usuario",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }

}