package com.daxen.mydancekmpsharedui.features.teacher.classes.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.daxen.mydancekmpsharedui.core.ui.LocalPadding
import com.daxen.mydancekmpsharedui.core.ui.composables.ErrorComponent
import com.daxen.mydancekmpsharedui.core.ui.composables.LoadingComponent
import com.daxen.mydancekmpsharedui.data.academy.classes.models.BaseClassModel
import com.daxen.mydancekmpsharedui.data.academy.classes.models.SpecificClassModel
import com.daxen.mydancekmpsharedui.data.academy.classes.models.WeeklyClassModel
import com.daxen.mydancekmpsharedui.features.teacher.classes.viewmodel.ClassDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassDetailScreen(
    viewModel: ClassDetailViewModel,
    classId: String,
    isWeekly: Boolean,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    
    LaunchedEffect(classId, isWeekly) {
        viewModel.loadClass(classId, isWeekly)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de Clase") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            contentDescription = "Volver atrás"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    LoadingComponent("Cargando detalles de la clase...")
                }
                uiState.error != null -> {
                    ErrorComponent(
                        message = uiState.error ?: "Error al cargar la clase",
                        onRetry = { viewModel.loadClass(classId, isWeekly) }
                    )
                }
                else -> {
                    val classModel = if (uiState.isWeeklyClass) {
                        uiState.weeklyClass
                    } else {
                        uiState.specificClass
                    }
                    
                    if (classModel != null) {
                        ClassDetailContent(
                            classModel = classModel, 
                            isWeekly = uiState.isWeeklyClass,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(LocalPadding.current.normal)
                                .verticalScroll(rememberScrollState())
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ClassDetailContent(
    classModel: BaseClassModel,
    isWeekly: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Cabecera con nombre y tipo
        ClassHeaderSection(
            name = classModel.name,
            isWeekly = isWeekly
        )

        HorizontalDivider()

        // Información básica
        ClassInfoSection(
            classModel = classModel,
            isWeekly = isWeekly
        )

        HorizontalDivider()

        // Status y plazas disponibles
        ClassStatusSection(
            status = classModel.status,
            availableSpots = classModel.availableSpots
        )
        
        HorizontalDivider()
        
        // Estudiantes (solo visibles para profesores)
//        StudentsSection(students = classModel.data.students)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ClassHeaderSection(
    name: String,
    isWeekly: Boolean
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Chip(
            onClick = { },
            colors = ChipDefaults.chipColors(
                backgroundColor = if (isWeekly)
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                else
                    MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
                contentColor = if (isWeekly)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.secondary
            )
        ) {
            Text(
                text = if (isWeekly) "Clase semanal" else "Clase específica",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun ClassInfoSection(
    classModel: BaseClassModel,
    isWeekly: Boolean
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Información de la clase",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        
        InfoItem(
            icon = Icons.Default.Schedule,
            label = "Hora",
            value = classModel.hour
        )
        
        if (isWeekly && classModel is WeeklyClassModel) {
            InfoItem(
                icon = Icons.Default.DateRange,
                label = "Día de la semana",
                value = when (classModel.dayOfWeek) {
                    "MONDAY" -> "Lunes"
                    "TUESDAY" -> "Martes"
                    "WEDNESDAY" -> "Miércoles"
                    "THURSDAY" -> "Jueves"
                    "FRIDAY" -> "Viernes"
                    "SATURDAY" -> "Sábado"
                    "SUNDAY" -> "Domingo"
                    else -> classModel.dayOfWeek
                }
            )
        } else if (!isWeekly && classModel is SpecificClassModel) {
            InfoItem(
                icon = Icons.Default.Event,
                label = "Fecha",
                value = classModel.date
            )
        }
    }
}

//@Composable
//fun StudentsSection(
//    students: List<com.daxen.mydancekmpsharedui.data.teacher.classes.models.StudentModel>
//) {
//    Column(
//        modifier = Modifier.fillMaxWidth(),
//        verticalArrangement = Arrangement.spacedBy(16.dp)
//    ) {
//        Text(
//            text = "Estudiantes",
//            style = MaterialTheme.typography.titleMedium,
//            fontWeight = FontWeight.Bold
//        )
//
//        if (students.isEmpty()) {
//            Text(
//                text = "No hay estudiantes registrados en esta clase",
//                style = MaterialTheme.typography.bodyMedium,
//                color = MaterialTheme.colorScheme.onSurfaceVariant
//            )
//        } else {
//            students.forEach { student ->
//                StudentItem(student = student)
//            }
//        }
//    }
//}

//@Composable
//fun StudentItem(
//    student: com.daxen.mydancekmpsharedui.data.teacher.classes.models.StudentModel
//) {
//    Row(
//        modifier = Modifier.fillMaxWidth(),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Surface(
//            modifier = Modifier.size(40.dp),
//            shape = CircleShape,
//            color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)
//        ) {
//            Box(
//                contentAlignment = Alignment.Center
//            ) {
//                Icon(
//                    imageVector = Icons.Default.Person,
//                    contentDescription = null,
//                    tint = MaterialTheme.colorScheme.secondary
//                )
//            }
//        }
//
//        Spacer(modifier = Modifier.width(16.dp))
//
//        Text(
//            text = student.name,
//            style = MaterialTheme.typography.bodyLarge
//        )
//    }
//}

@Composable
fun ClassStatusSection(
    status: String,
    availableSpots: Int
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Estado",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        
        val statusDisplayText = when(status.lowercase()) {
            "available" -> "Disponible"
            "waitlist" -> "Lista de espera"
            else -> status
        }
        
        val statusColor = when(status.lowercase()) {
            "available" -> MaterialTheme.colorScheme.primary
            "waitlist" -> MaterialTheme.colorScheme.tertiary
            else -> MaterialTheme.colorScheme.onSurface
        }
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = when(status.lowercase()) {
                    "available" -> Icons.Default.CheckCircle
                    "waitlist" -> Icons.Default.AccessTime
                    else -> Icons.Default.Info
                },
                contentDescription = null,
                tint = statusColor
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Text(
                text = statusDisplayText,
                style = MaterialTheme.typography.bodyLarge,
                color = statusColor
            )
        }
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.People,
                contentDescription = null
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Text(
                text = "Plazas disponibles: $availableSpots",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun InfoItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
} 