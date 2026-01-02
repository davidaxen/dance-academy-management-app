package com.daxen.mydancekmpsharedui.features.academy.teachers.listing.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.daxen.mydancekmpsharedui.core.ui.composables.ErrorComponent
import com.daxen.mydancekmpsharedui.core.ui.composables.LoadingComponent
import com.daxen.mydancekmpsharedui.data.teachers.model.Teacher
import com.daxen.mydancekmpsharedui.features.academy.teachers.listing.ui.components.TeachersList

@Composable
fun TeachersListingScreen(
    viewModel: TeachersListingViewModel,
    onTeacherClick: (teacher: Teacher) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.refreshTeachers()
    }
    
    Box(modifier = Modifier.fillMaxSize()) {
        when (val state = uiState) {
            is TeachersListingUiState.Loading -> LoadingComponent(
                "Cargando profesores...",
            )
            is TeachersListingUiState.Success -> TeachersList(
                teachers = state.teachers,
                onTeacherClick = onTeacherClick,
                onRefresh = { viewModel.refreshTeachers() },
                isRefreshing = isRefreshing
            )
            is TeachersListingUiState.Empty -> EmptyContentWithRefresh(
                isFiltered = state.isFiltered,
                onRefresh = { viewModel.refreshTeachers() },
                isRefreshing = isRefreshing
            )
            is TeachersListingUiState.Error -> {
                ErrorComponent(
                    message = state.message,
                    onRetry = { viewModel.refreshTeachers() }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EmptyContentWithRefresh(
    isFiltered: Boolean,
    onRefresh: () -> Unit,
    isRefreshing: Boolean
) {
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = Modifier.fillMaxSize()
    ) {
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
                    "No se encontraron profesores con esa búsqueda" 
                else 
                    "No hay profesores disponibles",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = if (isFiltered)
                    "Intenta con otros términos de búsqueda"
                else
                    "Agrega profesores a tu academia para comenzar",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
} 