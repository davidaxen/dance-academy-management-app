package com.daxen.mydancekmpsharedui.features.calendar.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.daxen.mydancekmpsharedui.core.ui.LocalPadding
import com.daxen.mydancekmpsharedui.features.calendar.ui.models.CalendarMonth
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.plus

@Composable
fun CalendarGrid(
    currentMonth: CalendarMonth,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    hasReservations: (LocalDate) -> Boolean,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // Días de la semana
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf("L", "M", "X", "J", "V", "S", "D").forEach { day ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .padding(LocalPadding.current.extraTiny),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = day,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Grid de días
        val firstDayOfMonth = currentMonth.firstDayOfMonth
        val firstDayOfWeek = firstDayOfMonth.dayOfWeek.ordinal // 0-6 (Lunes-Domingo)
        
        // Calcular el primer día visible del calendario
        val startDate = firstDayOfMonth.minus(firstDayOfWeek, DateTimeUnit.DAY)
        
        // Calcular el último día visible del calendario
        val lastDayOfMonth = currentMonth.lastDayOfMonth
        val lastDayOfWeek = lastDayOfMonth.dayOfWeek.ordinal // 0-6 (Lunes-Domingo)
        val endDate = lastDayOfMonth.plus(6 - lastDayOfWeek, DateTimeUnit.DAY)
        
        var currentDate = startDate
        while (currentDate <= endDate) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                repeat(7) { _ ->
                    if (currentDate in firstDayOfMonth..lastDayOfMonth) {
                        CalendarDay(
                            date = currentDate,
                            isSelected = currentDate == selectedDate,
                            hasReservations = hasReservations(currentDate),
                            onClick = { onDateSelected(it) },
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        Box(modifier = Modifier.weight(1f))
                    }
                    currentDate = currentDate.plus(1, DateTimeUnit.DAY)
                }
            }
        }
    }
} 