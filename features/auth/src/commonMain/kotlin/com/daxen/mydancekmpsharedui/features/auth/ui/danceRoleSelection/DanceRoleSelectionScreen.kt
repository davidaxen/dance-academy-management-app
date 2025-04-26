package com.daxen.mydancekmpsharedui.features.auth.ui.danceRoleSelection

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.daxen.mydancekmpsharedui.core.ui.LocalPadding
import com.daxen.mydancekmpsharedui.core.ui.theme.*
import com.daxen.mydancekmpsharedui.data.user.model.DanceRole
import com.daxen.mydancekmpsharedui.features.auth.ui.components.*

@Composable
internal fun DanceRoleSelectionScreen(
    viewModel: DanceRoleSelectionViewModel,
    onNavigateBack: () -> Unit,
    onNavigateNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedRole by viewModel.selectedRole.collectAsState()
    val danceRoleSelectionState by viewModel.danceRoleSelectionState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        CurvedBackgroundFull()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopStart),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            TopBarBackSection(onNavigateBack)

            AuthTitleAndSubtitle(
                title = "Rol",
                subtitle = "¿Cuál es tu rol principal de baile?"
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth(0.70f)
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AuthCard {
                RoleCard(
                    title = "Leader",
                    description = "Guía los pasos y marca el ritmo",
                    icon = Icons.Default.Star,
                    isSelected = selectedRole == DanceRole.LEADER,
                    onClick = {
                        viewModel.onRoleSelected(DanceRole.LEADER)
                    }
                )

                Row {
                    HorizontalDivider(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .align(Alignment.CenterVertically)
                            .weight(1f),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Text(
                        text = "o",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    HorizontalDivider(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .align(Alignment.CenterVertically)
                            .weight(1f),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }

                RoleCard(
                    title = "Follower",
                    description = "Sigue la guía y responde con estilo",
                    icon = Icons.Default.Favorite,
                    isSelected = selectedRole == DanceRole.FOLLOWER,
                    onClick = {
                        viewModel.onRoleSelected(DanceRole.FOLLOWER)
                    }
                )

                when (danceRoleSelectionState) {
                    is DanceRoleSelectionState.Error -> {
                        Text(
                            text = (danceRoleSelectionState as DanceRoleSelectionState.Error).message,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        AuthButton(
                            text = "Continuar",
                            onClick = {
                                viewModel.saveDanceRole()
                                onNavigateNext()
                            },
                            isLoading = false,
                            isDisabled = selectedRole == null,
                            modifier = Modifier.fillMaxWidth(0.8f),
                        )
                    }
                    else -> {
                        AuthButton(
                            text = "Continuar",
                            onClick = {
                                viewModel.saveDanceRole()
                                onNavigateNext()
                            },
                            isLoading = danceRoleSelectionState == DanceRoleSelectionState.Loading,
                            isDisabled = selectedRole == null,
                            modifier = Modifier.fillMaxWidth(0.8f),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RoleCard(
    title: String,
    description: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundModifier = if (isSelected) {
        Modifier.background(
            brush = Brush.verticalGradient(
                colors = listOf(
                    PrimaryBlue,
                    PrimaryBlueLight,
                    PrimaryBlueDark,
                )
            )
        )
    } else {
        Modifier.background(MaterialTheme.colorScheme.surface.copy(alpha = 0.1f))
    }.fillMaxWidth().padding(LocalPadding.current.tiny)
    
    val contentColor by animateColorAsState(
        targetValue = if (isSelected) 
            MaterialTheme.colorScheme.onPrimaryContainer
        else 
            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
        animationSpec = tween(durationMillis = 300)
    )
    
    val descriptionColor by animateColorAsState(
        targetValue = if (isSelected) 
            MaterialTheme.colorScheme.onPrimaryContainer
        else 
            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
        animationSpec = tween(durationMillis = 300)
    )

    Card(
        onClick = onClick,
        modifier = Modifier
            .width(180.dp)
            .padding(LocalPadding.current.small),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 12.dp else 4.dp
        )
    ) {
        Column(
            modifier = backgroundModifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(48.dp),
                tint = descriptionColor
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = contentColor
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = descriptionColor,
                textAlign = TextAlign.Center
            )
        }
    }
}