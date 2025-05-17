package com.daxen.mydancekmpsharedui.features.student.reservation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daxen.mydancekmpsharedui.core.ui.LocalPadding
import com.daxen.mydancekmpsharedui.core.ui.composables.ErrorComponent
import com.daxen.mydancekmpsharedui.core.ui.composables.LoadingComponent
import com.daxen.mydancekmpsharedui.data.classes.models.SpecificClassModel
import com.daxen.mydancekmpsharedui.data.classes.models.WeeklyClassModel
import com.daxen.mydancekmpsharedui.features.student.reservation.ui.components.DanceClassCard
import com.daxen.mydancekmpsharedui.features.student.reservation.ui.components.WeekSelectorSection
import com.daxen.mydancekmpsharedui.features.student.reservation.utils.ClassOrigin
import com.daxen.mydancekmpsharedui.features.student.reservation.utils.Constants
import com.daxen.mydancekmpsharedui.features.student.reservation.utils.DisplayClass
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import org.koin.compose.viewmodel.koinViewModel
import androidx.compose.foundation.BorderStroke
import androidx.compose.material.AlertDialog
import androidx.compose.material.TextButton

@Composable
internal fun ReservationScreen(
    viewModel: ReservationViewModel = koinViewModel()
) {
    val classesState = viewModel.classesListState.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        when (val state = classesState.value) {
            is ClassesListUiState.Loading -> {
                LoadingComponent(text = "Cargando clases...")
            }

            is ClassesListUiState.Empty -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(Icons.Default.Info, "", modifier = Modifier.size(50.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "No existen clases")
                }
            }

            is ClassesListUiState.Success -> {
                ClassScheduleScreen(
                    weeklyClasses = state.weekly,
                    specificClasses = state.specific,
                    viewModel = viewModel
                )
            }

            is ClassesListUiState.Error -> {
                ErrorComponent(message = "Error al cargar las clases", onRetry = {
                    viewModel.reloadClasses()
                })
            }
        }

    }
}

@Composable
private fun ClassScheduleScreen(
    weeklyClasses: List<WeeklyClassModel>,
    specificClasses: List<SpecificClassModel>,
    viewModel: ReservationViewModel
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()
    val currentWeek by viewModel.currentWeek.collectAsState()
    val canGoBack by viewModel.canGoBack.collectAsState()
    val userReservations by viewModel.userReservations.collectAsState()
    val today = viewModel.today

    val filteredClasses = remember(specificClasses, weeklyClasses, selectedDate, userReservations) {
        val dayOfWeek = selectedDate.dayOfWeek.name
        val specific = specificClasses.filter { it.date == selectedDate.toString() }.map {
                val teacherName = if (it.data.teachers.isNotEmpty()) it.data.teachers[0].name else ""
                val isReserved = viewModel.isClassReserved(it.data.id)
                val reservationId = if (isReserved) viewModel.getReservationId(it.data.id) else ""
                DisplayClass(
                    id = it.data.id,
                    hour = it.data.hour,
                    name = it.data.name,
                    teacherId = it.data.teacherId,
                    teacherName = teacherName,
                    status = it.data.status,
                    availableSpots = it.data.availableSpots,
                    origin = ClassOrigin.SPECIFIC,
                    isReserved = isReserved,
                    reservationId = reservationId
                )
            }

        val weekly = weeklyClasses.filter { it.dayOfWeek == dayOfWeek }.map {
                val teacherName = if (it.data.teachers.isNotEmpty()) it.data.teachers[0].name else ""
                val isReserved = viewModel.isClassReserved(it.data.id)
                val reservationId = if (isReserved) viewModel.getReservationId(it.data.id) else ""
                DisplayClass(
                    id = it.data.id,
                    hour = it.data.hour,
                    name = it.data.name,
                    teacherId = it.data.teacherId,
                    teacherName = teacherName,
                    status = it.data.status,
                    availableSpots = it.data.availableSpots,
                    origin = ClassOrigin.WEEKLY,
                    isReserved = isReserved,
                    reservationId = reservationId
                )
            }

        (specific + weekly).sortedBy { LocalTime.parse(it.hour) }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(LocalPadding.current.small)
        ) {
            TopScheduleSection(
                selectedDate = selectedDate,
                today = today,
                onClickToday = { viewModel.goToToday() })

            WeekSelectorSection(
                currentWeek = currentWeek,
                selectedDate = selectedDate,
                today = today,
                canGoBack = canGoBack,
                onDateSelected = { viewModel.selectDate(it) },
                onPreviousWeek = { viewModel.goToPreviousWeek() },
                onNextWeek = { viewModel.goToNextWeek() },
            )

        }

        ClassesListSection(
            classes = filteredClasses, 
            onReserveClick = { classModel ->
                viewModel.reserveClass(
                    studentId = currentUser.uid,
                    classId = classModel.id,
                    name = classModel.name,
                    hour = classModel.hour,
                )
            },
            onCancelReservation = { classModel ->
                viewModel.cancelReservation(classId = classModel.id)
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ClassesListSection(
    classes: List<DisplayClass>,
    onReserveClick: (DisplayClass) -> Unit,
    onCancelReservation: (DisplayClass) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedClass by remember { mutableStateOf<DisplayClass?>(null) }
    var showCancelConfirmation by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxWidth().padding(top = LocalPadding.current.tiny)
            .padding(horizontal = LocalPadding.current.normal)
    ) {
        if (classes.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Sin clases",
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
                Text(
                    text = "No hay clases disponibles para este día",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        } else {
            LazyColumn {
                items(classes) { danceClass ->
                    DanceClassCard(danceClass, openBottomSheet = {
                        showBottomSheet = true
                        selectedClass = danceClass
                    })
                }
            }

            if (showBottomSheet && selectedClass != null) {
                ModalBottomSheet(
                    onDismissRequest = {
                        showBottomSheet = false
                    },
                    sheetState = sheetState,
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 0.dp,
                    dragHandle = null
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = LocalPadding.current.big)
                            .padding(bottom = LocalPadding.current.normal)
                    ) {
                        // Header con título y hora
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                                .padding(LocalPadding.current.normal)
                        ) {
                            Column {
                                Text(
                                    text = selectedClass!!.name,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.DateRange,
                                        contentDescription = "Hora",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = selectedClass!!.hour,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(LocalPadding.current.normal))

                        // Detalles de la clase
                        Column {
                            // Profesor
                            DetailRow(
                                icon = Icons.Default.Person,
                                title = "Profesor",
                                content = selectedClass!!.teacherName.ifEmpty { "Profesor sin asignar" }
                            )

                            Spacer(modifier = Modifier.height(LocalPadding.current.small))

                            // Tipo de clase
                            DetailRow(
                                icon = Icons.Default.Info,
                                title = "Tipo",
                                content = if (selectedClass!!.origin == ClassOrigin.WEEKLY) "Clase semanal" else "Clase especial"
                            )

                            Spacer(modifier = Modifier.height(LocalPadding.current.normal))

                            // Botón de reservar o cancelar
                            if (selectedClass!!.isReserved) {
                                TextButton(
                                    onClick = {
                                        showCancelConfirmation = true
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    border = BorderStroke(1.dp, Color.Red),
                                    shape = RoundedCornerShape(12.dp),
                                ) {
                                    Text(
                                        text = "Cancelar reserva",
                                        color = Color.Red,
                                        modifier = Modifier.padding(vertical = LocalPadding.current.extraTiny)
                                    )
                                }
                            } else {
                                Button(
                                    onClick = {
                                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                                            if (!sheetState.isVisible) {
                                                showBottomSheet = false
                                                selectedClass?.let {
                                                    onReserveClick(it)
                                                }
                                                selectedClass = null
                                            }
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary,
                                        contentColor = MaterialTheme.colorScheme.onPrimary
                                    )
                                ) {
                                    Text(
                                        text = "Reservar clase",
                                        modifier = Modifier.padding(vertical = LocalPadding.current.extraTiny)
                                    )
                                }
                            }
                        }
                    }
                }
            }
            
            // Diálogo de confirmación para cancelar reserva
            if (showCancelConfirmation && selectedClass != null) {
                AlertDialog(
                    onDismissRequest = { showCancelConfirmation = false },
                    title = { Text("Cancelar reserva") },
                    text = { Text("¿Estás seguro que deseas cancelar tu reserva para la clase ${selectedClass!!.name}?") },
                    backgroundColor = Color.White,
                    confirmButton = {
                        TextButton(
                            onClick = {
                                showCancelConfirmation = false
                                scope.launch { sheetState.hide() }.invokeOnCompletion {
                                    if (!sheetState.isVisible) {
                                        showBottomSheet = false
                                        selectedClass?.let {
                                            onCancelReservation(it)
                                        }
                                        selectedClass = null
                                    }
                                }
                            }
                        ) {
                            Text("Sí, cancelar", color = Color.Red)
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                showCancelConfirmation = false
                            }
                        ) {
                            Text("No")
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun DetailRow(
    icon: ImageVector,
    title: String,
    content: String,
    contentColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Text(
                text = content,
                style = MaterialTheme.typography.bodyLarge,
                color = contentColor
            )
        }
    }
}

@Composable
private fun TopScheduleSection(
    selectedDate: LocalDate, today: LocalDate, onClickToday: () -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth().padding(vertical = LocalPadding.current.small), contentAlignment = Alignment.CenterEnd) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            // Nombre del Mes Actual
            Text(
                text = "${selectedDate.dayOfMonth} de ${Constants.monthNames[selectedDate.monthNumber - 1]} ${selectedDate.year}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
        }

        // Botón "Hoy"
        val isDisabled = selectedDate == today
        Text(text = "Hoy",
            color = if (isDisabled) Color.Gray.copy(alpha = 0.5f) else Color.Blue,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .clickable(enabled = !isDisabled,
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }) { onClickToday() })

    }
}