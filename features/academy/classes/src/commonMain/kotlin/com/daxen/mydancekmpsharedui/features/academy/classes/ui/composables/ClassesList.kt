package com.daxen.mydancekmpsharedui.features.academy.classes.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.dropUnlessResumed
import com.daxen.mydancekmpsharedui.core.ui.LocalPadding
import com.daxen.mydancekmpsharedui.data.academy.classes.models.SpecificClassModel
import com.daxen.mydancekmpsharedui.data.academy.classes.models.TeacherModel
import com.daxen.mydancekmpsharedui.data.academy.classes.models.WeeklyClassModel
import kotlinx.datetime.DayOfWeek

data class ClassesGroup(
    val dayOfWeek: DayOfWeek,
    val weeklyClasses: List<WeeklyClassModel>,
    val specificClasses: List<SpecificClassModel>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassesList(
    classesGroups: List<ClassesGroup>,
    onClassClick: (classData: Any, isWeekly: Boolean) -> Unit,
    onRefresh: () -> Unit,
    isRefreshing: Boolean
) {
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
        ) {
            classesGroups.forEach { group ->
                val combinedClasses = mutableListOf<Pair<Boolean, Any>>()
                group.weeklyClasses.forEach { 
                    combinedClasses.add(Pair(true, it))
                }
                group.specificClasses.forEach {
                    combinedClasses.add(Pair(false, it))
                }
                
                items(combinedClasses) { (isWeekly, classData) ->
                    if (isWeekly) {
                        val weeklyClass = classData as WeeklyClassModel
                        ClassListItem(
                            name = weeklyClass.name,
                            hour = weeklyClass.hour,
                            teachers = weeklyClass.teachers,
                            status = weeklyClass.status,
                            isWeekly = true,
                            onClick = dropUnlessResumed {
                                onClassClick(weeklyClass, true)
                            }
                        )
                    } else {
                        val specificClass = classData as SpecificClassModel
                        ClassListItem(
                            name = specificClass.name,
                            hour = specificClass.hour,
                            teachers = specificClass.teachers,
                            status = specificClass.status,
                            isWeekly = false,
                            dateInfo = "Fecha: ${specificClass.date}",
                            onClick = dropUnlessResumed {
                                onClassClick(specificClass, false)
                            }
                        )
                    }
                    
                    HorizontalDivider(
                        thickness = 0.5.dp,
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                }
                
                // Si no hay clases para este día, mostrar mensaje
                if (combinedClasses.isEmpty()) {
                    item {
                        Text(
                            text = "No hay clases programadas para este día",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
private fun ClassListItem(
    name: String,
    hour: String,
    teachers: List<TeacherModel>,
    status: String,
    isWeekly: Boolean,
    dateInfo: String = "",
    onClick: () -> Unit
) {
    val padding = LocalPadding.current
    
    ListItem(
        headlineContent = {
            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium
            )
        },
        supportingContent = {
            Column {
                Row {
                    Text(
                        text = hour,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    if (isWeekly) {
                        Text(
                            text = "Semanal",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    } else {
                        Text(
                            text = "Específica",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
                
                if (dateInfo.isNotEmpty()) {
                    Text(
                        text = dateInfo,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Text(
                    text = "Profesor${if (teachers.size > 1) "es" else ""}: ${teachers.joinToString { it.name }}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        leadingContent = null,
        trailingContent = {
            Row {
                ClassStatusIndicator(status = status)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Filled.ChevronRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = padding.tiny)
    )
}

@Composable
private fun ClassStatusIndicator(status: String) {
    val backgroundColor = when(status.lowercase()) {
        "available" -> Color(0xFFE8F5E9) // Verde claro
        "waitlist" -> Color(0xFFFFF8E1)  // Ámbar claro
        else -> Color(0xFFEFEFEF)         // Gris claro por defecto
    }
    
    val textColor = when(status.lowercase()) {
        "available" -> Color(0xFF2E7D32) // Verde oscuro
        "waitlist" -> Color(0xFFF57F17)  // Ámbar oscuro
        else -> Color(0xFF757575)         // Gris oscuro por defecto
    }
    
    val text = when(status.lowercase()) {
        "available" -> "Disponible"
        "waitlist" -> "Lista de Espera"
        else -> status
    }
    
    Surface(
        color = backgroundColor,
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = text,
            color = textColor,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}