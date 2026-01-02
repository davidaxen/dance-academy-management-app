package com.daxen.mydancekmpsharedui.features.academy.classes.ui.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.daxen.mydancekmpsharedui.core.ui.LocalPadding
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.plus

@Composable
fun WeekSelector(
    weekStartDate: LocalDate,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onPreviousWeekClick: () -> Unit,
    onNextWeekClick: () -> Unit,
    onTodayClick: () -> Unit = {},
    isSelectedDateToday: Boolean = false,
    modifier: Modifier = Modifier
) {
    val padding = LocalPadding.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(padding.small)
    ) {
        // Fila con mes/año y botón "Hoy"
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Texto "Hoy" clicable a la derecha
            Text(
                text = "Hoy",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                // El color depende de si está habilitado o no
                color = if (!isSelectedDateToday) 
                    MaterialTheme.colorScheme.primary 
                else 
                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 16.dp)
                    .clickable(
                        enabled = !isSelectedDateToday,
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onTodayClick() }
            )
            
            // Mostrar mes y año en el centro absoluto
            Text(
                text = getMesAnoText(selectedDate),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Spacer(modifier = Modifier.height(padding.small))

        // Fila de días de la semana
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onPreviousWeekClick,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Semana anterior",
                    modifier = Modifier.size(16.dp)
                )
            }

            // Mostrar todos los días de la semana
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Generar fechas para la semana actual (comenzando en lunes)
                val weekDates = generateWeekDates(weekStartDate)

                // Mostramos todos los días de la semana (índices 0-6)
                for (i in 0..6) {
                    val date = weekDates[i]
                    val isSelected = selectedDate.dayOfMonth == date.dayOfMonth &&
                            selectedDate.month == date.month &&
                            selectedDate.year == date.year

                    DayItem(
                        date = date,
                        isSelected = isSelected,
                        onClick = { onDateSelected(date) }
                    )
                }
            }

            IconButton(
                onClick = onNextWeekClick,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Semana siguiente",
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
private fun DayItem(
    date: LocalDate,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val dayOfWeek = when (date.dayOfWeek) {
        DayOfWeek.MONDAY -> "L"
        DayOfWeek.TUESDAY -> "M"
        DayOfWeek.WEDNESDAY -> "X"
        DayOfWeek.THURSDAY -> "J"
        DayOfWeek.FRIDAY -> "V"
        DayOfWeek.SATURDAY -> "S"
        DayOfWeek.SUNDAY -> "D"
        else -> ""
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 1.dp, vertical = 2.dp)
    ) {
        // Día de la semana (L, M, X, J, V)
        Text(
            text = dayOfWeek,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium
        )

        // Número del día
        Surface(
            modifier = Modifier
                .size(28.dp),
            shape = CircleShape,
            color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
            border = if (isSelected) null else BorderStroke(
                1.dp,
                MaterialTheme.colorScheme.outlineVariant
            ),
            contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.clickable(onClick = onClick)) {
                Text(
                    text = date.dayOfMonth.toString(),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}

private fun getMesAnoText(date: LocalDate): String {
    val mes = when (date.month) {
        Month.JANUARY -> "Enero"
        Month.FEBRUARY -> "Febrero"
        Month.MARCH -> "Marzo"
        Month.APRIL -> "Abril"
        Month.MAY -> "Mayo"
        Month.JUNE -> "Junio"
        Month.JULY -> "Julio"
        Month.AUGUST -> "Agosto"
        Month.SEPTEMBER -> "Septiembre"
        Month.OCTOBER -> "Octubre"
        Month.NOVEMBER -> "Noviembre"
        Month.DECEMBER -> "Diciembre"
        else -> ""
    }

    return "$mes ${date.year}"
}

private fun generateWeekDates(mondayDate: LocalDate): List<LocalDate> {
    return (0..6).map { dayOffset ->
        mondayDate.plus(dayOffset, DateTimeUnit.DAY)
    }
} 