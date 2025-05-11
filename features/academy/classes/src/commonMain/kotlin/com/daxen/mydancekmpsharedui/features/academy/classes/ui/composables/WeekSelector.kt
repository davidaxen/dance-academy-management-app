package com.daxen.mydancekmpsharedui.features.academy.classes.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.daxen.mydancekmpsharedui.core.ui.LocalPadding
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month

@Composable
fun WeekSelector(
    startDate: LocalDate?,
    endDate: LocalDate?,
    onPreviousWeekClick: () -> Unit,
    onNextWeekClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val padding = LocalPadding.current
    
    Surface(
        modifier = modifier.fillMaxWidth(),
        shadowElevation = 4.dp,
        color = MaterialTheme.colorScheme.background
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding.normal),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onPreviousWeekClick) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "Semana anterior"
                )
            }
            
            Spacer(modifier = Modifier.width(padding.small))
            
            if (startDate != null && endDate != null) {
                val formattedDate = formatWeekRange(startDate, endDate)
                Text(
                    text = formattedDate,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.width(padding.small))
            
            IconButton(onClick = onNextWeekClick) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "Semana siguiente"
                )
            }
        }
    }
}

private fun formatWeekRange(startDate: LocalDate, endDate: LocalDate): String {
    val startMonth = getSpanishMonth(startDate.month)
    val endMonth = getSpanishMonth(endDate.month)
    
    return if (startDate.month == endDate.month) {
        "${startDate.dayOfMonth}–${endDate.dayOfMonth} de $startMonth"
    } else {
        "${startDate.dayOfMonth} de $startMonth – ${endDate.dayOfMonth} de $endMonth"
    }
}

private fun getSpanishMonth(month: Month): String {
    return when (month) {
        Month.JANUARY -> "enero"
        Month.FEBRUARY -> "febrero"
        Month.MARCH -> "marzo"
        Month.APRIL -> "abril"
        Month.MAY -> "mayo"
        Month.JUNE -> "junio"
        Month.JULY -> "julio"
        Month.AUGUST -> "agosto"
        Month.SEPTEMBER -> "septiembre"
        Month.OCTOBER -> "octubre"
        Month.NOVEMBER -> "noviembre"
        Month.DECEMBER -> "diciembre"
        else -> "enero"
    }
} 