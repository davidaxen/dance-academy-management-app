package com.daxen.mydancekmpsharedui.features.auth.ui.role

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
import com.daxen.mydancekmpsharedui.core.ui.LocalPadding
import com.daxen.mydancekmpsharedui.core.ui.theme.*
import com.daxen.mydancekmpsharedui.data.user.model.UserRole
import com.daxen.mydancekmpsharedui.features.auth.ui.components.CurvedBackground

@Composable
fun RoleSelectionScreen(
    onRoleSelected: (UserRole) -> Unit,
    modifier: Modifier = Modifier
) {
    var showMenu by remember { mutableStateOf(false) }
    var showExpandedBackground by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        showExpandedBackground = true
    }
    
    Box(modifier = modifier) {
        AnimatedBackground(showExpandedBackground)
        
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

            Spacer(modifier = Modifier.height(48.dp))

            RoleButton(
                title = "Estudiante",
                description = "Para reservar clases y acceder al contenido de tus academias",
                icon = Icons.Default.Person,
                iconColor = MaterialTheme.colorScheme.primary,
                onClick = { onRoleSelected(UserRole.STUDENT) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            RoleButton(
                title = "Profesor",
                description = "Para gestionar clases y compartir contenido con alumnos",
                icon = Icons.Default.School,
                iconColor = MaterialTheme.colorScheme.secondary,
                onClick = { onRoleSelected(UserRole.TEACHER) }
            )

            Spacer(modifier = Modifier.height(16.dp))

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

@Composable
private fun AnimatedBackground(expanded: Boolean) {
    val progress by animateFloatAsState(
        targetValue = if (expanded) 1.5f else 0f,
        animationSpec = tween(
            durationMillis = 4000,
            easing = FastOutSlowInEasing
        ),
        label = "backgroundAnimation"
    )

    Canvas(
        modifier = Modifier.fillMaxSize()
//            .fillMaxWidth()
//            .fillMaxHeight(0.5f)
    ) {
        // Primera curva (principal)
        val mainPath = Path().apply {
            moveTo(0f, size.height * (0.7f + (0.4f * progress)))
            cubicTo(
                size.width * 0.3f,
                size.height * (0.15f + (0.6f * progress)),
                size.width * 0.7f,
                size.height * (0.6f + (0.6f * progress)),
                size.width, 
                size.height * (0.4f + (0.6f * progress))
            )
            lineTo(size.width, 0f)
            lineTo(0f, 0f)
            close()
        }

        // Segunda curva (detalle)
        val detailPath = Path().apply {
            moveTo(0f, size.height * (0.2f + (0.5f * progress)))
            cubicTo(
                size.width * 0.3f,
                size.height * (0.05f + (0.15f * progress)),
                size.width * 0.5f,
                size.height * (0.3f + (0.5f * progress)),
                size.width, 
                size.height * (0.1f + (0.3f * progress))
            )
            lineTo(size.width, 0f)
            lineTo(0f, 0f)
            close()
        }

        // Tercera curva (accent)
        val accentPath = Path().apply {
            moveTo(0f, size.height * (0.1f + (0.2f * progress)))
            cubicTo(
                size.width * 0.1f,
                size.height * (0.05f + (0.05f * progress)),
                size.width * 0.3f,
                size.height * (0.15f + (0.25f * progress)),
                size.width, 
                size.height * (0.05f + (0.15f * progress))
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
                end = Offset(size.width, size.height * (0.5f + (0.7f * progress)))
            )
        )

        drawPath(
            path = detailPath,
            brush = Brush.linearGradient(
                colors = listOf(SecondaryPurple.copy(alpha = 0.7f), PrimaryBlueLight.copy(alpha = 0.7f)),
                start = Offset(0f, 0f),
                end = Offset(size.width, size.height * (0.3f + (0.5f * progress)))
            )
        )

        drawPath(
            path = accentPath,
            brush = Brush.linearGradient(
                colors = listOf(PrimaryBlueLight.copy(alpha = 0.5f), PrimaryBlue.copy(alpha = 0.5f)),
                start = Offset(0f, 0f),
                end = Offset(size.width, size.height * (0.2f + (0.2f * progress)))
            )
        )

        // Añadir algunos círculos decorativos
        drawCircle(
            color = PrimaryBlue.copy(alpha = 0.2f * progress),
            radius = size.width * (0.2f + (0.1f * progress)),
            center = Offset(size.width * 0.2f, size.height * (0.1f + (0.2f * progress)))
        )

        drawCircle(
            color = SecondaryPurple.copy(alpha = 0.2f * progress),
            radius = size.width * (0.15f + (0.05f * progress)),
            center = Offset(size.width * 0.8f, size.height * (0.05f + (0.15f * progress)))
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