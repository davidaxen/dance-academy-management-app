package com.daxen.mydancekmpsharedui.features.calendar.ui

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.daxen.mydancekmpsharedui.features.calendar.ui.components.CalendarGrid
import com.daxen.mydancekmpsharedui.features.calendar.ui.components.MonthHeader
import com.daxen.mydancekmpsharedui.features.calendar.ui.models.CalendarMonth
import kotlinx.datetime.*
import kotlinx.datetime.TimeZone

@Composable
fun CalendarScreen() {
    val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    var selectedDate by remember { mutableStateOf(currentDate) }
    var currentMonth by remember { mutableStateOf(CalendarMonth(currentDate.year, currentDate.month)) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
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

        Spacer(modifier = Modifier.height(16.dp))

        CalendarGrid(
            currentMonth = currentMonth,
            selectedDate = selectedDate,
            onDateSelected = { date ->
                selectedDate = date
            }
        )
    }
}

