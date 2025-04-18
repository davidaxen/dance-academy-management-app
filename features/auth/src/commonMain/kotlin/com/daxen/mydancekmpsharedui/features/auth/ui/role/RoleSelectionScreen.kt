package com.daxen.mydancekmpsharedui.features.auth.ui.role

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.daxen.mydancekmpsharedui.core.ui.LocalPadding
import com.daxen.mydancekmpsharedui.core.ui.theme.*
import com.daxen.mydancekmpsharedui.data.user.model.UserRole

@Composable
fun RoleSelectionScreen(
    onRoleSelected: (UserRole) -> Unit,
    modifier: Modifier = Modifier
) {
    var showMenu by remember { mutableStateOf(false) }
    var showExpandedBackground by remember { mutableStateOf(false) }
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        showExpandedBackground = true
        visible = true
    }
    
    Box(modifier = modifier) {
        AnimatedBackground(showExpandedBackground)

        AnimatedVisibility(
            visible = visible,
            enter = slideInVertically(
                animationSpec = tween(durationMillis = 1500, easing = EaseIn)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // Menú de usuario
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    contentAlignment = Alignment.TopEnd
                ) {
                    IconButton(
                        onClick = { showMenu = true }
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Menú de usuario",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("prueba@gmail.com") },
                            onClick = { showMenu = false }
                        )
                        Divider()
                        DropdownMenuItem(
                            text = { Text("Cerrar sesión") },
                            onClick = { showMenu = false }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "¡Bienvenido!",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onPrimary,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Ya estás a nada de ser parte de la app\n\n Primero necesitamos saber que tipo de usuario serás ",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(60.dp))

                RoleButton(
                    title = "Estudiante",
                    description = "Para reservar clases y acceder al contenido de tus academias",
                    icon = Icons.Default.Person,
                    iconColor = MaterialTheme.colorScheme.primary,
                    onClick = { onRoleSelected(UserRole.STUDENT) }
                )

                Spacer(modifier = Modifier.height(24.dp))

                RoleButton(
                    title = "Profesor",
                    description = "Para gestionar clases y compartir contenido con alumnos",
                    icon = Icons.Default.School,
                    iconColor = MaterialTheme.colorScheme.secondary,
                    onClick = { onRoleSelected(UserRole.TEACHER) }
                )

                Spacer(modifier = Modifier.height(24.dp))

                RoleButton(
                    title = "Academia",
                    description = "Representantes o administradores de academias de baile",
                    icon = Icons.Default.Business,
                    iconColor = MaterialTheme.colorScheme.tertiary,
                    onClick = { onRoleSelected(UserRole.ACADEMY) }
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
            durationMillis = 4000,
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
        Box(modifier = Modifier.fillMaxWidth().clickable { onClick() }){
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