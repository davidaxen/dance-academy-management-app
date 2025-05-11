package com.daxen.mydancekmpsharedui.features.academy.classes.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.daxen.mydancekmpsharedui.core.ui.LocalPadding
import com.daxen.mydancekmpsharedui.data.academy.classes.models.WeeklyClassModel
import kotlinx.datetime.DayOfWeek

@Composable
fun DayClassesList(
    dayOfWeek: DayOfWeek,
    classes: List<WeeklyClassModel>,
    modifier: Modifier = Modifier
) {
    val padding = LocalPadding.current
    
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(padding.normal)
    ) {
        Text(
            text = getDayName(dayOfWeek),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(padding.tiny))
        
        Divider()
        
        Spacer(modifier = Modifier.height(padding.small))
        
        if (classes.isEmpty()) {
            Text(
                text = "No hay clases programadas para este día",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            classes.forEach { classModel ->
                ClassCard(
                    name = classModel.name,
                    hour = classModel.hour,
                    teachers = classModel.teachers,
                    status = classModel.status
                )
                
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

private fun getDayName(dayOfWeek: DayOfWeek): String {
    return when (dayOfWeek) {
        DayOfWeek.MONDAY -> "Lunes"
        DayOfWeek.TUESDAY -> "Martes"
        DayOfWeek.WEDNESDAY -> "Miércoles"
        DayOfWeek.THURSDAY -> "Jueves"
        DayOfWeek.FRIDAY -> "Viernes"
        DayOfWeek.SATURDAY -> "Sábado"
        DayOfWeek.SUNDAY -> "Domingo"
        else -> "Lunes"
    }
} 