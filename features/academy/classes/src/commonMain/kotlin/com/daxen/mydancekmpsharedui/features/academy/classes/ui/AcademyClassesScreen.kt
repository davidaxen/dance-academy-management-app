package com.daxen.mydancekmpsharedui.features.academy.classes.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.daxen.mydancekmpsharedui.core.ui.composables.ErrorComponent
import com.daxen.mydancekmpsharedui.core.ui.composables.LoadingComponent
import com.daxen.mydancekmpsharedui.features.academy.classes.ui.composables.ClassesList
import com.daxen.mydancekmpsharedui.features.academy.classes.ui.composables.WeekSelector
import com.daxen.mydancekmpsharedui.features.academy.classes.ui.models.AcademyClassesUiState
import com.daxen.mydancekmpsharedui.features.academy.classes.viewmodel.AcademyClassesViewModel

@Composable
fun AcademyClassesScreen(
    viewModel: AcademyClassesViewModel,
    onClassClick: (classData: Any, isWeekly: Boolean) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val selectedWeekStartDate by viewModel.selectedWeekStartDate.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()
    
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            WeekSelector(
                weekStartDate = selectedWeekStartDate,
                selectedDate = selectedDate,
                onDateSelected = { date -> viewModel.onDateSelected(date) },
                onPreviousWeekClick = { viewModel.moveWeekBackward() },
                onNextWeekClick = { viewModel.moveWeekForward() }
            )
            
            Box(
                modifier = Modifier.weight(1f)
            ) {
                when (val state = uiState) {
                    is AcademyClassesUiState.Loading -> LoadingComponent("Cargando clases...")
                    
                    is AcademyClassesUiState.Success -> ClassesList(
                        classesGroups = state.classesGroups,
                        onClassClick = { classData, isWeekly -> 
                            onClassClick(classData, isWeekly)
                        },
                        onRefresh = { viewModel.refreshClasses() },
                        isRefreshing = isRefreshing
                    )
                    
                    is AcademyClassesUiState.Empty -> EmptyClassesContent(
                        isFiltered = state.isFiltered,
                        onRefresh = { viewModel.refreshClasses() },
                        isRefreshing = isRefreshing
                    )
                    
                    is AcademyClassesUiState.Error -> ErrorComponent(
                        message = state.message,
                        onRetry = { viewModel.refreshClasses() }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EmptyClassesContent(
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
                    "No se encontraron clases con esa búsqueda" 
                else 
                    "No hay clases programadas para este día",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = if (isFiltered)
                    "Intenta con otros términos de búsqueda"
                else
                    "Prueba con otro día o agrega clases a tu academia",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
} 