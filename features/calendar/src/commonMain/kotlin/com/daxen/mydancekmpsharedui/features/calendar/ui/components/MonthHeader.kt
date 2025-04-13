package com.daxen.mydancekmpsharedui.features.calendar.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.daxen.mydancekmpsharedui.features.calendar.ui.models.CalendarMonth
import kotlinx.datetime.Month

@Composable
fun MonthHeader(
    currentMonth: CalendarMonth,
    onPreviousMonthClick: () -> Unit,
    onNextMonthClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium),
        color = Color.White,
        shadowElevation = 8.dp,
        tonalElevation = 10.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onPreviousMonthClick,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Mes anterior"
                )
            }

            Text(
                text = "${getMonthName(currentMonth.month)} ${currentMonth.year}",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.primary
            )

            IconButton(
                onClick = onNextMonthClick,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Mes siguiente"
                )
            }
        }
    }
}

private fun getMonthName(month: Month): String {
    return when (month) {
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
        else -> throw IllegalArgumentException("Mes no válido")
    }
} 