package com.daxen.mydancekmpsharedui.features.academy.students.listing.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Search
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
import com.daxen.mydancekmpsharedui.core.ui.composables.ErrorComponent
import com.daxen.mydancekmpsharedui.core.ui.composables.LoadingComponent

@Composable
fun StudentsListingScreen(
    viewModel: StudentsListingViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Box(modifier = Modifier.fillMaxSize()) {
        when (val state = uiState) {
            is StudentsListingUiState.Loading -> LoadingComponent(
                "Cargando estudiantes...",
            )
            is StudentsListingUiState.Success -> StudentsList(students = state.students)
            is StudentsListingUiState.Empty -> EmptyContent(isFiltered = state.isFiltered)
            is StudentsListingUiState.Error -> {
                ErrorComponent(
                    message = state.message,
                    onRetry = { viewModel.refreshStudents() }
                )
            }
        }
    }
}

@Composable
private fun EmptyContent(isFiltered: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = if (isFiltered) Icons.Default.Search else Icons.Default.ErrorOutline,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = if (isFiltered) 
                "No se encontraron estudiantes con esa búsqueda" 
            else 
                "No hay estudiantes disponibles",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = if (isFiltered)
                "Intenta con otros términos de búsqueda"
            else
                "Agrega estudiantes a tu academia para comenzar",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun StudentsList(students: List<StudentModel>) {
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