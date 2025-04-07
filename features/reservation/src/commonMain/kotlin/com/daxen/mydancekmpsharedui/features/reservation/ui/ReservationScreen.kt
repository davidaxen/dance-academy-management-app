package com.daxen.mydancekmpsharedui.features.reservation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material3.ModalBottomSheetProperties
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daxen.mydancekmpsharedui.core.ui.LocalPadding
import com.daxen.mydancekmpsharedui.core.ui.composables.ErrorComponent
import com.daxen.mydancekmpsharedui.core.ui.composables.LoadingComponent
import com.daxen.mydancekmpsharedui.data.classes.models.SpecificClassModel
import com.daxen.mydancekmpsharedui.data.classes.models.WeeklyClassModel
import com.daxen.mydancekmpsharedui.features.reservation.ui.components.DanceClassCard
import com.daxen.mydancekmpsharedui.features.reservation.ui.components.WeekSelectorSection
import com.daxen.mydancekmpsharedui.features.reservation.utils.ClassOrigin
import com.daxen.mydancekmpsharedui.features.reservation.utils.Constants
import com.daxen.mydancekmpsharedui.features.reservation.utils.DisplayClass
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import org.koin.compose.viewmodel.koinViewModel

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
    val today = viewModel.today

    val filteredClasses = remember(specificClasses, weeklyClasses, selectedDate) {
        val dayOfWeek = selectedDate.dayOfWeek.name
        val specific = specificClasses.filter { it.date == selectedDate.toString() }.map {
                DisplayClass(
                    id = it.data.id,
                    hour = it.data.hour,
                    name = it.data.name,
                    teacherId = it.data.teacherId,
                    status = it.data.status,
                    availableSpots = it.data.availableSpots,
                    origin = ClassOrigin.SPECIFIC
                )
            }

        val weekly = weeklyClasses.filter { it.dayOfWeek == dayOfWeek }.map {
                DisplayClass(
                    id = it.data.id,
                    hour = it.data.hour,
                    name = it.data.name,
                    teacherId = it.data.teacherId,
                    status = it.data.status,
                    availableSpots = it.data.availableSpots,
                    origin = ClassOrigin.WEEKLY
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

        ClassesListSection(classes = filteredClasses, onReserveClick = { classModel ->
            viewModel.reserveClass(
                academyId = "CJK3TNrlIeIXdKYeI5Ee",
                studentId = currentUser.uid,
                classId = classModel.id
            )
        })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ClassesListSection(
    classes: List<DisplayClass>,
    onReserveClick: (DisplayClass) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedClass by remember { mutableStateOf<DisplayClass?>(null) }

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
                    imageVector = Icons.Default.Info, // Icono de "sin eventos"
                    contentDescription = "Sin clases",
                    modifier = Modifier.size(64.dp),
                    tint = Color.Gray
                )
                Text(
                    text = "No hay clases disponibles para este día",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }
        } else {
            // Mostrar la lista de clases si hay
            LazyColumn {
                items(classes) { danceClass ->
                    DanceClassCard(danceClass, onReserveClick = onReserveClick, openBottomSheet = {
                        showBottomSheet = true
                        selectedClass = danceClass
                    })
                }
            }

            if (showBottomSheet && selectedClass != null) {
                ModalBottomSheet(
//                    modifier = Modifier.fillMaxHeight(),
                    containerColor = Color.White,
                    onDismissRequest = {
                        showBottomSheet = false
                    },
                    sheetState = sheetState,
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = LocalPadding.current.big)
                            .padding(bottom = LocalPadding.current.normal)
                    ) {
                        ClassReservationContent(classItem = selectedClass!!, onReserveClick = { })

                        // Botón de reservar
                        Button(
                            onClick = {
                                scope.launch { sheetState.hide() }.invokeOnCompletion {
                                    if (!sheetState.isVisible) {
                                        showBottomSheet = false
                                        selectedClass = null
                                    }
                                }
                            }, colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Blue, contentColor = Color.White
                            ), modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20)
                        ) {
                            Text(
                                "Reservar clase",
                                color = Color.White,
                                modifier = Modifier.padding(vertical = LocalPadding.current.extraTiny)
                            )
                        }
                    }

                }
            }
        }
    }
}

@Composable
fun ClassReservationContent(
    classItem: DisplayClass, onReserveClick: () -> Unit, modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        // Título
        Text(
            text = classItem.name,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Hora + Día
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 6.dp)
        ) {
            Icon(
                Icons.Default.DateRange,
                contentDescription = "Hora",
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
//                text = "${classItem.hour} · ${classItem.day.lowercase().replaceFirstChar { it.uppercase() }}",
                text = "${classItem.hour} · ${classItem.origin}",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        // Profesor
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 24.dp)
        ) {
            Icon(
                Icons.Default.Person,
                contentDescription = "Profesor",
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Profesor: Marley & Leo", style = MaterialTheme.typography.bodyMedium
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