package com.daxen.mydancekmpsharedui.features.calendar.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.daxen.mydancekmpsharedui.features.calendar.ui.models.CalendarMonth
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus

@Composable
fun CalendarGrid(
    currentMonth: CalendarMonth,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // Días de la semana
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf("L", "M", "X", "J", "V", "S", "D").forEach { day ->
                Text(
                    text = day,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 4.dp),
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Grid de días
        var currentDate = currentMonth.firstDayOfMonth
        while (currentDate <= currentMonth.lastDayOfMonth) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                repeat(7) { dayOfWeek ->
                    val date = currentDate.plus(dayOfWeek, DateTimeUnit.DAY)
                    if (date >= currentMonth.firstDayOfMonth && date <= currentMonth.lastDayOfMonth) {
                        CalendarDay(
                            date = date,
                            isSelected = date == selectedDate,
                            onClick = { onDateSelected(date) },
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        Box(modifier = Modifier.weight(1f))
                    }
                }
                currentDate = currentDate.plus(7, DateTimeUnit.DAY)
            }
        }
    }
} 