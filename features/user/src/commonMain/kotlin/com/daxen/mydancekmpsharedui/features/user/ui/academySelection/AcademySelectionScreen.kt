package com.daxen.mydancekmpsharedui.features.user.ui.academySelection

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.daxen.mydancekmpsharedui.core.ui.LocalPadding
import com.daxen.mydancekmpsharedui.features.user.ui.academySelection.components.AcademyCard
import com.daxen.mydancekmpsharedui.features.user.ui.academySelection.components.InvitationCard
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size

@Composable
fun AcademySelectionScreen(
    viewModel: AcademySelectionViewModel,
) {
    val selectedTab by viewModel.selectedTab.collectAsState()
    val academies by viewModel.academies.collectAsState()
    val invitations by viewModel.invitations.collectAsState()
    val loadingInvitations by viewModel.loadingInvitations.collectAsState()

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
            selectedTabIndex = selectedTab,
            indicator = { tabPositions ->
                SecondaryIndicator(
                    modifier = Modifier
                        .tabIndicatorOffset(tabPositions[selectedTab]),
                    height = 2.dp,
                    color = MaterialTheme.colorScheme.onSecondary
                )
            },
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    icon = { Icon(tabIcons[index], contentDescription = title) },
                    selected = selectedTab == index,
                    onClick = { viewModel.onTabSelected(index) },
                    selectedContentColor = if (selectedTab == index)
                        MaterialTheme.colorScheme.onBackground
                    else
                        MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.background(
                        if (selectedTab == index)
                            MaterialTheme.colorScheme.background
                        else
                            MaterialTheme.colorScheme.primary
                    ),
                )
            }
        }

        when (selectedTab) {
            0 -> {
                // Lista de academias
                if (academies.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(LocalPadding.current.normal),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "No hay academias",
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(LocalPadding.current.normal))
                        Text(
                            text = "No estás matriculado en ninguna academia",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(LocalPadding.current.small))
                        Text(
                            text = "Cuando te matricules en la academia, aparecerá aquí",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(top = LocalPadding.current.small)
                    ) {
                        items(academies) { academy ->
                            AcademyCard(
                                name = academy.name,
                                location = academy.location,
                                imageUrl = academy.imageUrl,
                                schedule = academy.schedule,
                                role = academy.role.toSpanishText(),
                                onClick = { /* TODO: Navegar a la academia */ }
                            )
                        }
                    }
                }
            }
            1 -> {
                // Lista de invitaciones
                if (invitations.isEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "No hay invitaciones",
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(LocalPadding.current.normal))
                        Text(
                            text = "No tienes invitaciones pendientes",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(LocalPadding.current.small))
                        Text(
                            text = "Cuando una academia te invite, aparecerá aquí",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(top = LocalPadding.current.small)
                    ) {
                        items(invitations) { invitation ->
                            InvitationCard(
                                academyName = invitation.name,
                                academyImage = invitation.imageUrl,
                                role = invitation.role.toSpanishText(),
                                onAccept = { viewModel.acceptInvitation(invitation) },
                                onReject = { viewModel.rejectInvitation(invitation) },
                                isLoading = loadingInvitations.contains(invitation)
                            )
                        }
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