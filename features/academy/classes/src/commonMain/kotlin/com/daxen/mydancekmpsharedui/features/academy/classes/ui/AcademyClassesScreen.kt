package com.daxen.mydancekmpsharedui.features.academy.classes.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.daxen.mydancekmpsharedui.core.ui.LocalPadding
import com.daxen.mydancekmpsharedui.data.academy.classes.models.SpecificClassModel
import com.daxen.mydancekmpsharedui.data.academy.classes.models.WeeklyClassModel
import com.daxen.mydancekmpsharedui.features.academy.classes.ui.composables.DayClassesList
import com.daxen.mydancekmpsharedui.features.academy.classes.ui.composables.WeekSelector
import com.daxen.mydancekmpsharedui.features.academy.classes.viewmodel.AcademyClassesViewModel
import kotlinx.datetime.DayOfWeek

@Composable
fun AcademyClassesScreen(
    viewModel: AcademyClassesViewModel,
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            WeekSelector(
                startDate = state.selectedWeekStartDate,
                endDate = state.selectedWeekEndDate,
                onPreviousWeekClick = { viewModel.moveWeekBackward() },
                onNextWeekClick = { viewModel.moveWeekForward() }
            )
            
            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (state.errorMessage != null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.errorMessage ?: "Error desconocido",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                WeekClassesContent(
                    weeklyClasses = state.weeklyClasses,
                    specificClasses = state.specificClasses
                )
            }
        }
    }
}

@Composable
private fun WeekClassesContent(
    weeklyClasses: List<WeeklyClassModel>,
    specificClasses: List<SpecificClassModel>
) {
    // Agrupar clases por día de la semana
    val classesByDay = DayOfWeek.entries.associateWith { dayOfWeek ->
        val dayString = when (dayOfWeek) {
            DayOfWeek.MONDAY -> "MONDAY"
            DayOfWeek.TUESDAY -> "TUESDAY"
            DayOfWeek.WEDNESDAY -> "WEDNESDAY"
            DayOfWeek.THURSDAY -> "THURSDAY"
            DayOfWeek.FRIDAY -> "FRIDAY"
            DayOfWeek.SATURDAY -> "SATURDAY"
            DayOfWeek.SUNDAY -> "SUNDAY"
            else -> "MONDAY"
        }
        
        // Filtrar clases para este día
        weeklyClasses.filter { it.dayOfWeek == dayString }
    }
    
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(DayOfWeek.entries.toTypedArray()) { dayOfWeek ->
            val classes = classesByDay[dayOfWeek] ?: emptyList()
            
            DayClassesList(
                dayOfWeek = dayOfWeek,
                classes = classes
            )
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
} 