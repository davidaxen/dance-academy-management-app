package com.daxen.mydancekmpsharedui.features.calendar.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.daxen.mydancekmpsharedui.core.ui.LocalPadding
import com.daxen.mydancekmpsharedui.features.calendar.ui.components.CalendarGrid
import com.daxen.mydancekmpsharedui.features.calendar.ui.components.MonthHeader
import com.daxen.mydancekmpsharedui.features.calendar.ui.components.ReservedClassesList
import com.daxen.mydancekmpsharedui.features.calendar.ui.viewmodel.CalendarViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel = koinViewModel()
) {
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
                .padding(horizontal = LocalPadding.current.normal)
                .padding(top = LocalPadding.current.normal)
        ) {
            MonthHeader(
                currentMonth = currentMonth,
                onPreviousMonthClick = { viewModel.onPreviousMonthClick() },
                onNextMonthClick = { viewModel.onNextMonthClick() },
            )

            Spacer(modifier = Modifier.height(8.dp))

            CalendarGrid(
                currentMonth = currentMonth,
                selectedDate = selectedDate,
                onDateSelected = { viewModel.onDateSelected(it) },
                hasReservations = { viewModel.hasReservations(it) },
            )
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

