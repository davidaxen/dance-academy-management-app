package com.daxen.mydancekmpsharedui.features.teacher.classes.ui

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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.daxen.mydancekmpsharedui.core.ui.composables.ErrorComponent
import com.daxen.mydancekmpsharedui.core.ui.composables.LoadingComponent
import com.daxen.mydancekmpsharedui.features.teacher.classes.ui.composables.ClassesList
import com.daxen.mydancekmpsharedui.features.teacher.classes.ui.composables.WeekSelector
import com.daxen.mydancekmpsharedui.features.teacher.classes.ui.models.TeacherClassesUiState
import com.daxen.mydancekmpsharedui.features.teacher.classes.viewmodel.TeacherClassesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherClassesScreen(
    viewModel: TeacherClassesViewModel,
    onClassClick: (classData: Any, isWeekly: Boolean) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val selectedWeekStartDate by viewModel.selectedWeekStartDate.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()
    val today by viewModel.today.collectAsState()
    
    // Calcular si el día seleccionado es hoy dentro de Compose para que reaccione a cambios
    val isSelectedDateToday by remember {
        derivedStateOf {
            selectedDate.year == today.year &&
            selectedDate.month == today.month &&
            selectedDate.dayOfMonth == today.dayOfMonth
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Clases") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
            )
        }
    ) { padding ->
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
                onNextWeekClick = { viewModel.moveWeekForward() },
                onTodayClick = { viewModel.goToToday() },
                isSelectedDateToday = isSelectedDateToday
            )
            
            Box(
                modifier = Modifier.weight(1f)
            ) {
                when (val state = uiState) {
                    is TeacherClassesUiState.Loading -> LoadingComponent("Cargando clases...")
                    
                    is TeacherClassesUiState.Success -> ClassesList(
                        classesGroups = state.classesGroups,
                        onRefresh = { viewModel.refreshClasses() },
                        onClassClick = { classData, isWeekly ->
                            onClassClick(classData, isWeekly)
                        },
                        isRefreshing = isRefreshing
                    )
                    
                    is TeacherClassesUiState.Empty -> EmptyClassesContent(
                        isFiltered = state.isFiltered,
                        onRefresh = { viewModel.refreshClasses() },
                        isRefreshing = isRefreshing
                    )
                    
                    is TeacherClassesUiState.Error -> ErrorComponent(
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
                    "No tienes clases programadas para este día",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = if (isFiltered)
                    "Intenta con otros términos de búsqueda"
                else
                    "Prueba con otro día o contacta con tu academia",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}