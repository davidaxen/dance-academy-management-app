package com.daxen.mydancekmpsharedui.features.auth.ui.userRoleSelection

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.lifecycle.compose.dropUnlessResumed
import com.daxen.mydancekmpsharedui.core.ui.LocalPadding
import com.daxen.mydancekmpsharedui.core.ui.theme.*
import com.daxen.mydancekmpsharedui.data.user.model.UserRole
import com.daxen.mydancekmpsharedui.features.auth.ui.components.AuthTitleAndSubtitle

@Composable
fun UserRoleSelectionScreen(
    viewModel: UserRoleSelectionViewModel,
    onRoleSelected: (UserRole) -> Unit,
    onLogOut: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentEmail by viewModel.currentEmail.collectAsState()
    var showAnimation by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (!showAnimation) {
            showAnimation = true
        }
    }
    
    Box(modifier = modifier) {
        AnimatedBackground(showAnimation)

        AnimatedVisibility(
            visible = showAnimation,
            enter = slideInVertically(
                animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
            ),
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                AuthTitleAndSubtitle(
                    title = "¡Bienvenido!",
                    subtitle = "¿Cómo vas a usar la app?",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = LocalPadding.current.large)
                        .padding(horizontal = LocalPadding.current.normal)
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ){
                    RoleButton(
                        title = "Estudiante",
                        description = "Para reservar clases y acceder al contenido de tus academias",
                        icon = Icons.Default.Person,
                        iconColor = MaterialTheme.colorScheme.primary,
                        onClick = {
                            viewModel.onRoleSelected(UserRole.STUDENT)
                            onRoleSelected(UserRole.STUDENT)
                        }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    RoleButton(
                        title = "Profesor",
                        description = "Para gestionar clases y compartir contenido con tus alumnos",
                        icon = Icons.Default.School,
                        iconColor = MaterialTheme.colorScheme.secondary,
                        onClick = {
                            viewModel.onRoleSelected(UserRole.TEACHER)
                            onRoleSelected(UserRole.TEACHER)
                        }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    RoleButton(
                        title = "Academia",
                        description = "Representantes o administradores de academias de baile",
                        icon = Icons.Default.Business,
                        iconColor = MaterialTheme.colorScheme.tertiary,
                        onClick = {
                            viewModel.onRoleSelected(UserRole.ACADEMY)
                            onRoleSelected(UserRole.ACADEMY)
                        }
                    )
                }
                UserMenu(
                    userEmail = currentEmail ?: "",
                    modifier = Modifier.fillMaxWidth()
                ) {
                    onLogOut()
                }
            }
        }
    }
}

@Composable
private fun UserMenu(
    userEmail: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(modifier = modifier.background(MaterialTheme.colorScheme.background).padding(vertical = LocalPadding.current.tiny)) {
        Column(modifier = Modifier.padding(start = LocalPadding.current.tiny)) {
            Text(
                text = "Sesión iniciada como:",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = userEmail,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.weight(1f))
        TextButton(
            onClick = onClick,
            shape = RoundedCornerShape(0),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.background,
            ),
        ) {
            Text(
                text = "Cerrar sesión",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}
@Composable
private fun RoleButton(
    title: String,
    description: String,
    icon: ImageVector,
    iconColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Box(modifier = Modifier.fillMaxWidth().clickable(
            onClick = dropUnlessResumed {
                onClick()
            }
        )){
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(24.dp)
                )

                
                Column(modifier = Modifier.padding(start = LocalPadding.current.small).weight(1f)){
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

@Composable
private fun AnimatedBackground(expanded: Boolean) {
    val progress by animateFloatAsState(
        targetValue = if (expanded) 1f else 0f,
        animationSpec = tween(
            durationMillis = 1000,
            easing = FastOutSlowInEasing
        ),
    )

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f + (0.5f * progress))
    ) {
        // Primera curva (principal)
        val increment = 0.5f * progress
        val mainPath = Path().apply {
            val yStart = lerp(start = size.height * 0.7f, stop = size.height, fraction = progress)
            val controlY1 = lerp(start = size.height * 0.3f, stop = size.height, fraction = progress)
            val controlY2 = lerp(start = size.height * 1.2f, stop = size.height, fraction = progress)
            val endY = lerp(start = size.height * 0.8f, stop = size.height, fraction = progress)

            moveTo(0f, yStart)
            cubicTo(
                size.width * 0.3f, controlY1,
                size.width * 0.7f, controlY2,
                size.width, endY
            )
            lineTo(size.width, 0f)
            lineTo(0f, 0f)
            close()
        }

        // Segunda curva (detalle)
        val detailPath = Path().apply {
            moveTo(0f, size.height * (0.5f + (increment * 0.4f)))
            cubicTo(
                size.width * 0.2f,
                size.height * (0.2f + (increment * 0.4f)),
                size.width * 0.5f,
                size.height * (0.8f + (increment * 0.4f)),
                size.width,
                size.height * (0.4f + (increment * 0.4f))
            )
            lineTo(size.width, 0f)
            lineTo(0f, 0f)
            close()
        }

        // Tercera curva (accent)
        val accentPath = Path().apply {
            moveTo(0f, size.height * (0.3f + (increment * 0.2f)))
            cubicTo(
                size.width * 0.1f,
                size.height * (0.1f + (increment * 0.2f)),
                size.width * 0.3f,
                size.height * (0.4f + (increment * 0.2f)),
                size.width,
                size.height * (0.2f + (increment * 0.2f))
            )
            lineTo(size.width, 0f)
            lineTo(0f, 0f)
            close()
        }

        // Dibujar las curvas con diferentes gradientes
        drawPath(
            path = mainPath,
            brush = Brush.linearGradient(
                colors = listOf(PrimaryBlue, SecondaryPurple),
                start = Offset(0f, 0f),
                end = Offset(size.width, size.height * (1.2f + increment))
            )
        )

        drawPath(
            path = detailPath,
            brush = Brush.linearGradient(
                colors = listOf(SecondaryPurple.copy(alpha = 0.7f), PrimaryBlueLight.copy(alpha = 0.7f)),
                start = Offset(0f, 0f),
                end = Offset(size.width, size.height * (0.8f + (increment * 0.2f)))
            )
        )

        drawPath(
            path = accentPath,
            brush = Brush.linearGradient(
                colors = listOf(PrimaryBlueLight.copy(alpha = 0.5f), PrimaryBlue.copy(alpha = 0.5f)),
                start = Offset(0f, 0f),
                end = Offset(size.width, size.height * (0.4f + (increment * 0.4f)))
            )
        )

        // Añadir algunos círculos decorativos
        drawCircle(
            color = PrimaryBlue.copy(alpha = 0.2f * progress),
            radius = size.width * (0.2f + (increment * 0.2f)),
            center = Offset(size.width * 0.2f, size.height * (0.3f ))
        )

        drawCircle(
            color = SecondaryPurple.copy(alpha = 0.2f * progress),
            radius = size.width * (0.15f + (increment * 0.2f)),
            center = Offset(size.width * 0.8f, size.height * (0.2f))
        )
    }
}