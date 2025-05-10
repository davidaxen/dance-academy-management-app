package com.daxen.mydancekmpsharedui.features.academy.students.listing.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
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
import com.daxen.mydancekmpsharedui.features.academy.students.listing.ui.StudentModel

@Composable
fun StudentAvatar(
    student: StudentModel,
    modifier: Modifier = Modifier
) {
    val avatarSize = 40.dp

    if (student.profileImageUrl != null) {
        // Usar AsyncImage para cargar la imagen de perfil
        AsyncImage(
            model = student.profileImageUrl,
            contentDescription = "Foto de perfil de ${student.name}",
            contentScale = ContentScale.Crop,
            modifier = modifier
                .size(avatarSize)
                .clip(CircleShape)
        )
    } else {
        // Mostrar iniciales cuando no hay imagen
        Surface(
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
            shape = CircleShape,
            modifier = modifier.size(avatarSize)
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = getInitials(student.name, student.surnames),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

private fun getInitials(name: String, surnames: String): String {
    val firstInitial = name.firstOrNull()?.uppercase() ?: ""
    val lastInitial = surnames.split(" ").firstOrNull()?.firstOrNull()?.uppercase() ?: ""
    return "$firstInitial$lastInitial"
}