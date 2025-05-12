package com.daxen.mydancekmpsharedui.features.academy.classes.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.daxen.mydancekmpsharedui.core.ui.LocalPadding
import com.daxen.mydancekmpsharedui.core.ui.composables.LoadingComponent
import com.daxen.mydancekmpsharedui.data.teachers.model.Teacher
import com.daxen.mydancekmpsharedui.features.academy.classes.ui.components.DatePickerField
import com.daxen.mydancekmpsharedui.features.academy.classes.ui.components.TeacherSelector
import com.daxen.mydancekmpsharedui.features.academy.classes.viewmodel.ClassType
import com.daxen.mydancekmpsharedui.features.academy.classes.viewmodel.CreateClassViewModel

@Composable
fun CreateClassScreen(
    viewModel: CreateClassViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val filteredTeachers by viewModel.filteredTeachers.collectAsState()
    val teacherSearchQuery by viewModel.teacherSearchQuery.collectAsState()
    
    val scrollState = rememberScrollState()
    
    if (uiState.isLoading) {
        LoadingComponent("Cargando...")
        return
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        if (uiState.classCreated) {
            SuccessContent(
                className = uiState.lastCreatedClassName,
                onCreateAnotherClick = viewModel::resetForm,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(LocalPadding.current.normal)
            )
        } else {
            ClassFormContent(
                viewModel = viewModel, 
                filteredTeachers = filteredTeachers,
                teacherSearchQuery = teacherSearchQuery,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(LocalPadding.current.normal)
                    .verticalScroll(scrollState)
            )
        }
    }
}

@Composable
fun SuccessContent(
    className: String,
    onCreateAnotherClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            modifier = Modifier.size(120.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "¡Clase creada correctamente!",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "La clase '$className' ha sido guardada con éxito",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = onCreateAnotherClick,
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Text("Crear otra clase")
        }
    }
}

@Composable
fun ClassFormContent(
    viewModel: CreateClassViewModel,
    filteredTeachers: List<Teacher>,
    teacherSearchQuery: String,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(LocalPadding.current.normal)
    ) {
        ClassTypeSelector(
            selectedType = uiState.selectedClassType,
            onTypeSelected = viewModel::onClassTypeSelected
        )
        
        Spacer(modifier = Modifier.height(6.dp))
        
        // Formulario común para ambos tipos
        OutlinedTextField(
            value = uiState.name,
            onValueChange = viewModel::onNameChanged,
            label = { Text("Nombre de la clase") },
            modifier = Modifier.fillMaxWidth(),
            isError = uiState.showError && uiState.name.isEmpty()
        )
        
        OutlinedTextField(
            value = uiState.hour,
            onValueChange = viewModel::onHourChanged,
            label = { Text("Hora (HH:MM)") },
            placeholder = { Text("Ej: 19:30") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            isError = (uiState.showError && uiState.hour.isEmpty()) || !uiState.isHourFormatValid,
            supportingText = {
                if (!uiState.isHourFormatValid && uiState.hour.isNotEmpty()) {
                    Text(
                        text = "Formato incorrecto. Usa HH:MM (ej: 19:30)",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        )
        
        TeacherSelector(
            selectedTeachers = uiState.selectedTeachers,
            filteredTeachers = filteredTeachers,
            searchQuery = teacherSearchQuery,
            onSearchQueryChange = viewModel::onTeacherSearchQueryChanged,
            onAddTeacher = viewModel::addTeacher,
            onRemoveTeacher = viewModel::removeTeacher,
            showError = uiState.showError,
            modifier = Modifier.fillMaxWidth()
        )
        
        // Campos específicos según el tipo seleccionado
        when (uiState.selectedClassType) {
            ClassType.WEEKLY -> WeeklyClassForm(
                dayOfWeek = uiState.dayOfWeek,
                onDayOfWeekChanged = viewModel::onDayOfWeekChanged,
                showError = uiState.showError
            )
            ClassType.SPECIFIC -> SpecificClassForm(
                date = uiState.date,
                onDateChanged = viewModel::onDateChanged,
                showError = uiState.showError
            )
        }
        
        if (uiState.showError) {
            Text(
                text = uiState.error.ifEmpty { "Por favor, completa todos los campos requeridos correctamente" },
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
        
        Spacer(modifier = Modifier.height(LocalPadding.current.tiny))
        
        Button(
            onClick = viewModel::createClass,
            modifier = Modifier.fillMaxWidth().padding(bottom = LocalPadding.current.normal),
        ) {
            Text("Crear Clase")
        }
    }
}

@Composable
fun ClassTypeSelector(
    selectedType: ClassType,
    onTypeSelected: (ClassType) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(LocalPadding.current.normal)
    ) {
        ClassTypeCard(
            title = "Clase Semanal",
            description = "Se repite cada semana",
            selected = selectedType == ClassType.WEEKLY,
            onClick = { onTypeSelected(ClassType.WEEKLY) },
            modifier = Modifier.weight(1f)
        )
        
        ClassTypeCard(
            title = "Clase Específica",
            description = "En una fecha concreta",
            selected = selectedType == ClassType.SPECIFIC,
            onClick = { onTypeSelected(ClassType.SPECIFIC) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun ClassTypeCard(
    title: String,
    description: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (selected) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surface
    }
    
    val borderColor = if (selected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.outline
    }
    
    val textColor = if (selected) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onSurface
    }
    
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .selectable(
                selected = selected,
                onClick = onClick
            ),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        border = BorderStroke(
            width = 2.dp,
            color = borderColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (selected) 4.dp else 0.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RadioButton(
                selected = selected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(
                    selectedColor = MaterialTheme.colorScheme.primary
                )
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = textColor
            )
            
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = textColor
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeeklyClassForm(
    dayOfWeek: String,
    onDayOfWeekChanged: (String) -> Unit,
    showError: Boolean
) {
    val daysOfWeek = listOf("MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY")
    var expanded by remember { mutableStateOf(false) }
    
    val displayText = when (dayOfWeek) {
        "MONDAY" -> "Lunes"
        "TUESDAY" -> "Martes"
        "WEDNESDAY" -> "Miércoles"
        "THURSDAY" -> "Jueves"
        "FRIDAY" -> "Viernes"
        "SATURDAY" -> "Sábado"
        "SUNDAY" -> "Domingo"
        else -> "Selecciona un día"
    }
    
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = displayText,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryEditable, true),
            label = { Text("Día de la semana") },
            isError = showError && dayOfWeek.isEmpty()
        )
        
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = MaterialTheme.colorScheme.background
        ) {
            daysOfWeek.forEach { day ->
                val displayDay = when (day) {
                    "MONDAY" -> "Lunes"
                    "TUESDAY" -> "Martes"
                    "WEDNESDAY" -> "Miércoles"
                    "THURSDAY" -> "Jueves"
                    "FRIDAY" -> "Viernes"
                    "SATURDAY" -> "Sábado"
                    "SUNDAY" -> "Domingo"
                    else -> day
                }
                
                DropdownMenuItem(
                    text = { Text(displayDay, color = MaterialTheme.colorScheme.onBackground) },
                    onClick = {
                        onDayOfWeekChanged(day)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun SpecificClassForm(
    date: String,
    onDateChanged: (String) -> Unit,
    showError: Boolean
) {
    DatePickerField(
        value = date,
        onValueChange = onDateChanged,
        label = "Fecha",
        placeholder = "YYYY-MM-DD",
        error = showError && date.isEmpty(),
        modifier = Modifier.fillMaxWidth()
    )
} 