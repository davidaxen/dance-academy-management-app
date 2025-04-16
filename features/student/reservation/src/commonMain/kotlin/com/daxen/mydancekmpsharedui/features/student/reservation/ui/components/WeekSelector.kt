package com.daxen.mydancekmpsharedui.features.reservation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daxen.mydancekmpsharedui.features.reservation.utils.Constants
import kotlinx.datetime.LocalDate

@Composable
internal fun WeekSelectorSection(
    currentWeek: List<LocalDate>,
    selectedDate: LocalDate,
    today: LocalDate,
    canGoBack: Boolean,
    onDateSelected: (LocalDate) -> Unit,
    onPreviousWeek: () -> Unit,
    onNextWeek: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        WeekSelector(
            currentWeek = currentWeek,
            selectedDate = selectedDate,
            today = today,
            onDateSelected = onDateSelected,
            onPreviousWeek = onPreviousWeek,
            onNextWeek = onNextWeek,
            canGoBack = canGoBack
        )
    }
}

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
            enabled = canGoBack,
            modifier = Modifier.padding(horizontal = 4.dp)
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Semana anterior",
                tint = if (!canGoBack) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f) 
                       else MaterialTheme.colorScheme.onSurface
            )
        }

        Row(horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier.weight(1f)) {
            currentWeek.forEachIndexed { index, date ->
                val isPast = date < today
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clickable(
                            enabled = !isPast,
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                    ) { onDateSelected(date) }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                            .background(
                                if (date == selectedDate) MaterialTheme.colorScheme.primary else Color.Transparent,
                                shape = RoundedCornerShape(8.dp)
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Column(
                            modifier = Modifier.padding(4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = Constants.weekDaysShort[index],
                                fontSize = 16.sp,
                                fontWeight = if (date == selectedDate) FontWeight.Bold else FontWeight.Normal,
                                color = if (isPast) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                                else if (date == selectedDate) MaterialTheme.colorScheme.onPrimary
                                else MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = date.dayOfMonth.toString(),
                                fontSize = 18.sp,
                                fontWeight = if (date == selectedDate) FontWeight.Bold else FontWeight.Normal,
                                color = if (isPast) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                                else if (date == selectedDate) MaterialTheme.colorScheme.onPrimary
                                else MaterialTheme.colorScheme.onSurface
                            )
                        }

                    }
                }
            }
        }

        IconButton(onClick = onNextWeek, modifier = Modifier.padding(horizontal = 4.dp)) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowForward, 
                contentDescription = "Semana siguiente",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}