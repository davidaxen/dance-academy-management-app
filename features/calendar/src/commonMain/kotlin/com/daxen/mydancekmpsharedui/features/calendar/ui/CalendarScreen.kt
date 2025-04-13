package com.daxen.mydancekmpsharedui.features.calendar.ui

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.daxen.mydancekmpsharedui.core.ui.LocalPadding
import com.daxen.mydancekmpsharedui.features.calendar.ui.components.CalendarGrid
import com.daxen.mydancekmpsharedui.features.calendar.ui.components.MonthHeader
import com.daxen.mydancekmpsharedui.features.calendar.ui.components.ReservedClassesList
import com.daxen.mydancekmpsharedui.features.calendar.ui.models.CalendarMonth
import com.daxen.mydancekmpsharedui.features.calendar.ui.models.ReservedClass
import kotlinx.datetime.*
import kotlinx.datetime.TimeZone

@Composable
fun CalendarScreen() {
    val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    var selectedDate by remember { mutableStateOf(currentDate) }
    var currentMonth by remember { mutableStateOf(CalendarMonth(currentDate.year, currentDate.month)) }

    // Datos simulados
    val reservedClasses = remember {
        listOf(
            ReservedClass(
                id = "1",
                name = "Bachata Intermedio",
                date = currentDate,
                time = LocalTime(20, 0),
                teacher = "Lucía Gómez",
                room = "Sala 1"
            ),
            ReservedClass(
                id = "2",
                name = "Salsa Avanzado",
                date = currentDate,
                time = LocalTime(21, 30),
                teacher = "Carlos Pérez",
                room = "Sala 2"
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(LocalPadding.current.normal)
    ) {
        MonthHeader(
            currentMonth = currentMonth,
            onPreviousMonthClick = {
                currentMonth = currentMonth.previousMonth()
            },
            onNextMonthClick = {
                currentMonth = currentMonth.nextMonth()
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        CalendarGrid(
            currentMonth = currentMonth,
            selectedDate = selectedDate,
            onDateSelected = { date ->
                println("Fecha seleccionada: $date")
                selectedDate = date
            },
            hasReservations = { date ->
                reservedClasses.any { it.date == date }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        ReservedClassesList(
            selectedDate = selectedDate,
            reservedClasses = reservedClasses.filter { it.date == selectedDate },
            modifier = Modifier.weight(1f)
        )
    }
}

