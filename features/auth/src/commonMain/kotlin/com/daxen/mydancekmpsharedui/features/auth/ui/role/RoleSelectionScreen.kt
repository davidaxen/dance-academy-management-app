package com.daxen.mydancekmpsharedui.features.auth.ui.role

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.daxen.mydancekmpsharedui.data.user.model.UserRole
import com.daxen.mydancekmpsharedui.features.auth.ui.components.CurvedBackground

@Composable
fun RoleSelectionScreen(
    onRoleSelected: (UserRole) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        CurvedBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "¿Qué tipo de usuario eres?",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Elige una opción para continuar",
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
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(32.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column {
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
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
} 