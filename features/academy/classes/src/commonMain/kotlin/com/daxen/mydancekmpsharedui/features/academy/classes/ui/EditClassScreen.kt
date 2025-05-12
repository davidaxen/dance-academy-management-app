package com.daxen.mydancekmpsharedui.features.academy.classes.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.daxen.mydancekmpsharedui.core.ui.LocalPadding
import com.daxen.mydancekmpsharedui.core.ui.composables.ErrorComponent
import com.daxen.mydancekmpsharedui.core.ui.composables.LoadingComponent
import com.daxen.mydancekmpsharedui.data.teachers.model.Teacher
import com.daxen.mydancekmpsharedui.features.academy.classes.ui.components.TeacherSelector
import com.daxen.mydancekmpsharedui.features.academy.classes.viewmodel.EditClassViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditClassScreen(
    viewModel: EditClassViewModel,
    classId: String,
    isWeeklyClass: Boolean,
    onBackClick: () -> Unit,
    onUpdateSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val filteredTeachers by viewModel.filteredTeachers.collectAsState()
    val teacherSearchQuery by viewModel.teacherSearchQuery.collectAsState()
    
    val scrollState = rememberScrollState()
    
    LaunchedEffect(classId, isWeeklyClass) {
        viewModel.loadClass(classId, isWeeklyClass)
    }
    
    if (uiState.classUpdated) {
        LaunchedEffect(Unit) {
            onUpdateSuccess()
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Clase") },
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    LoadingComponent("Cargando datos de la clase...")
                }
                uiState.error.isNotEmpty() -> {
                    ErrorComponent(
                        message = uiState.error,
                        onRetry = { viewModel.loadClass(classId, isWeeklyClass) }
                    )
                }
                uiState.classUpdated -> {
                    SuccessContent(
                        className = uiState.name,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(LocalPadding.current.normal)
                    )
                }
                else -> {
                    EditClassFormContent(
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
    }
}

@Composable
fun SuccessContent(
    className: String,
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
            text = "¡Clase actualizada correctamente!",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "La clase '$className' ha sido actualizada con éxito",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditClassFormContent(
    viewModel: EditClassViewModel,
    filteredTeachers: List<Teacher>,
    teacherSearchQuery: String,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(LocalPadding.current.normal)
    ) {
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
        
        // Campo de estado
        ExposedDropdownMenuBox(
            expanded = false,
            onExpandedChange = {}
        ) {
            var expanded by remember { mutableStateOf(false) }
            
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it }
            ) {
                OutlinedTextField(
                    value = when (uiState.status.lowercase()) {
                        "available" -> "Disponible"
                        "waitlist" -> "Lista de espera"
                        else -> uiState.status
                    },
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(MenuAnchorType.PrimaryEditable, true),
                    label = { Text("Estado de la clase") }
                )
                
                ExposedDropdownMenu(
                    expanded = expanded,
                    containerColor = MaterialTheme.colorScheme.background,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Disponible") },
                        onClick = {
                            viewModel.onStatusChanged("available")
                            expanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Lista de espera") },
                        onClick = {
                            viewModel.onStatusChanged("waitlist")
                            expanded = false
                        }
                    )
                }
            }
        }
        
        // Campo de plazas disponibles
        OutlinedTextField(
            value = uiState.availableSpots,
            onValueChange = viewModel::onAvailableSpotsChanged,
            label = { Text("Plazas disponibles") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = uiState.showError && (uiState.availableSpots.toIntOrNull() == null),
            supportingText = {
                if (uiState.availableSpots.toIntOrNull() == null && uiState.availableSpots.isNotEmpty()) {
                    Text(
                        text = "Debe ser un número entero",
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
        if (uiState.isWeeklyClass) {
            WeeklyClassForm(
                dayOfWeek = uiState.dayOfWeek,
                onDayOfWeekChanged = viewModel::onDayOfWeekChanged,
                showError = uiState.showError
            )
        } else {
            SpecificClassForm(
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
            onClick = viewModel::updateClass,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = LocalPadding.current.normal),
        ) {
            Text("Actualizar Clase")
        }
    }
} 