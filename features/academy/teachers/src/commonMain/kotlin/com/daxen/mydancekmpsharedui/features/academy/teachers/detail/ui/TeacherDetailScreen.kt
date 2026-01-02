package com.daxen.mydancekmpsharedui.features.academy.teachers.detail.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.daxen.mydancekmpsharedui.data.teachers.model.Teacher
import com.daxen.mydancekmpsharedui.features.academy.teachers.components.TopBarWithBackButton

@Composable
fun TeacherDetailScreen(
    teacher: Teacher,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopBarWithBackButton(
                title = "Detalle del Profesor",
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
        ) {
            TeacherDetailContent(teacher = teacher)
        }
    }
}

@Composable
private fun TeacherDetailContent(teacher: Teacher) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Foto de perfil grande
        TeacherProfileImage(teacher)
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Nombre completo
        Text(
            text = "${teacher.name} ${teacher.lastName}",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        
        Text(
            text = "Profesor",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Tarjeta de información
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "INFORMACIÓN DEL PROFESOR",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Email
                InfoRow(
                    icon = Icons.Default.Email,
                    label = "Correo",
                    value = teacher.email
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                // Teléfono
                InfoRow(
                    icon = Icons.Default.Phone,
                    label = "Teléfono",
                    value = "${teacher.phoneNumberPrefix} ${teacher.phoneNumber}"
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                // Fecha de nacimiento
                InfoRow(
                    icon = Icons.Default.Cake,
                    label = "Fecha de nacimiento",
                    value = teacher.birthDate
                )
                
                if (teacher.joinedAt.isNotEmpty()) {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                    
                    // Fecha de incorporación
                    InfoRow(
                        icon = Icons.Default.CalendarMonth,
                        label = "Fecha de incorporación",
                        value = teacher.joinedAt
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun TeacherProfileImage(teacher: Teacher) {
    val profileSize = 150.dp
    
    if (teacher.profileImageUrl != null) {
        AsyncImage(
            model = teacher.profileImageUrl,
            contentDescription = "Foto de perfil de ${teacher.name}",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(profileSize)
                .clip(CircleShape)
        )
    } else {
        Surface(
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
            shape = CircleShape,
            modifier = Modifier.size(profileSize)
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = getInitials(teacher.name, teacher.lastName),
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun InfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = icon, 
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

private fun getInitials(name: String, surnames: String): String {
    val firstInitial = name.firstOrNull()?.uppercase() ?: ""
    val lastInitial = surnames.split(" ").firstOrNull()?.firstOrNull()?.uppercase() ?: ""
    return "$firstInitial$lastInitial"
} 