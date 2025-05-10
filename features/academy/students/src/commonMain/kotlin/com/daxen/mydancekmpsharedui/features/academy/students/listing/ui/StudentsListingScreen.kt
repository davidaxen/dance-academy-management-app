package com.daxen.mydancekmpsharedui.features.academy.students.listing.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.daxen.mydancekmpsharedui.core.ui.LocalPadding

@Composable
fun StudentsListingScreen(
    viewModel: StudentsListingViewModel
) {
    val students by viewModel.filteredStudents.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        itemsIndexed(students) { index, student ->
            StudentListItem(student = student)

            if (index < students.size - 1) {
                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@Composable
private fun StudentListItem(
    student: StudentModel
) {
    ListItem(
        leadingContent = {
            StudentAvatar(student = student)
        },
        headlineContent = {
            Text(
                text = "${student.name} ${student.surnames}",
                style = MaterialTheme.typography.titleMedium
            )
        },
        supportingContent = {
            Text(
                text = student.email,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        trailingContent = {
            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .clickable {

            }
            .padding(horizontal = LocalPadding.current.extraTiny)
    )
}

@Composable
private fun StudentAvatar(
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