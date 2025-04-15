package com.daxen.mydancekmpsharedui.features.calendar.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.daxen.mydancekmpsharedui.core.ui.LocalPadding
import com.daxen.mydancekmpsharedui.core.ui.composables.ErrorComponent
import com.daxen.mydancekmpsharedui.core.ui.composables.LoadingComponent
import com.daxen.mydancekmpsharedui.features.calendar.ui.components.CalendarGrid
import com.daxen.mydancekmpsharedui.features.calendar.ui.components.MonthHeader
import com.daxen.mydancekmpsharedui.features.calendar.ui.components.ReservedClassesList
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel = koinViewModel()
) {
    val daysWithReservations by viewModel.daysWithReservationsList.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadDaysWithReservations()
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        when (daysWithReservations) {
            is CalendarDatesListUiState.Loading -> {
                LoadingComponent(text = "Cargando calendario...")
            }
            is CalendarDatesListUiState.Error -> {
                ErrorComponent(message = "Error al cargar las clases reservadas en el calendario", onRetry = {
                    viewModel.loadDaysWithReservations()
                })
            }
            is CalendarDatesListUiState.Success, CalendarDatesListUiState.Empty -> {
                CalendarScreenSuccess(viewModel = viewModel, daysWithReservations = daysWithReservations)
            }
        }
    }
}

@Composable
fun CalendarScreenSuccess(viewModel: CalendarViewModel, daysWithReservations: CalendarDatesListUiState) {
    val selectedDate by viewModel.selectedDate.collectAsState()
    val currentMonth by viewModel.currentMonth.collectAsState()
    val reservedClasses by viewModel.reservedClasses.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(LocalPadding.current.normal)
        ) {
            MonthHeader(
                currentMonth = currentMonth,
                onPreviousMonthClick = { viewModel.onPreviousMonthClick() },
                onNextMonthClick = { viewModel.onNextMonthClick() },
            )

            Spacer(modifier = Modifier.height(8.dp))

            key(daysWithReservations) {
                CalendarGrid(
                    currentMonth = currentMonth,
                    selectedDate = selectedDate,
                    onDateSelected = { viewModel.onDateSelected(it) },
                    hasReservations = { viewModel.hasReservations(it) },
                )
            }
        }

        Divider()

        Spacer(modifier = Modifier.height(4.dp))

        ReservedClassesList(
            selectedDate = selectedDate,
            reservedClasses = viewModel.getReservedClassesForDate(selectedDate),
            modifier = Modifier.weight(1f)
        )
    }
}

