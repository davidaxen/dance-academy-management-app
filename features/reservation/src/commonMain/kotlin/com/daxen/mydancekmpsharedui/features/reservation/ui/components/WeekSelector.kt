package com.daxen.mydancekmpsharedui.features.reservation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.daxen.mydancekmpsharedui.features.reservation.utils.Constants
import kotlinx.datetime.LocalDate

@Composable
internal fun WeekSelector(
    currentWeek: List<LocalDate>,
    selectedDate: LocalDate,
    today: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onPreviousWeek: () -> Unit,
    onNextWeek: () -> Unit,
    canGoBack: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onPreviousWeek,
            enabled = canGoBack
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Semana anterior",
                tint = if (!canGoBack) Color.Gray else Color.Black
            )
        }

        Row(horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier.weight(1f)) {
            currentWeek.forEachIndexed { index, date ->
                val isPast = date < today
                Column(
                    modifier = Modifier
                        .padding(4.dp)
                        .clickable(enabled = !isPast) { onDateSelected(date) },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = Constants.weekDaysShort[index], // Inicial en español
                        fontWeight = if (date == selectedDate) FontWeight.Bold else FontWeight.Normal,
                        color = if (isPast) Color.Gray else if (date == selectedDate) Color.Blue else Color.Black
                    )
                    Text(
                        text = date.dayOfMonth.toString(),
                        fontWeight = if (date == selectedDate) FontWeight.Bold else FontWeight.Normal,
                        color = if (isPast) Color.Gray.copy(alpha = 0.5f) else if (date == selectedDate) Color.Blue else Color.Black
                    )
                }
            }
        }

        IconButton(onClick = onNextWeek) {
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Semana siguiente")
        }
    }
}