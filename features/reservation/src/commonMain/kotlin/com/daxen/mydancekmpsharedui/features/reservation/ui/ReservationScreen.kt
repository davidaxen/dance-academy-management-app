package com.daxen.mydancekmpsharedui.features.reservation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daxen.mydancekmpsharedui.core.ui.LocalPadding
import com.daxen.mydancekmpsharedui.data.classes.model.ClassModel
import com.daxen.mydancekmpsharedui.features.reservation.ui.components.DanceClassCard
import com.daxen.mydancekmpsharedui.features.reservation.ui.components.WeekSelector
import com.daxen.mydancekmpsharedui.features.reservation.utils.Constants
import kotlinx.datetime.LocalDate
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun ReservationScreen(
    viewModel: ReservationViewModel = koinViewModel()
) {
    val classesState = viewModel.classesListState.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        when (classesState.value) {
            is ClassesListUiState.Loading -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Cargando clases...")
                }
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
                val classes = (classesState.value as ClassesListUiState.Success).classes
                ClassScheduleScreen(classes, viewModel)
            }

            is ClassesListUiState.Error -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Error al cargar las clases",
                        color = Color.Red,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { }) {
                        Text("Reintentar")
                    }
                }
            }
        }

    }
}

@Composable
private fun ClassScheduleScreen(classes: List<ClassModel>, viewModel: ReservationViewModel) {
    val selectedDate by viewModel.selectedDate.collectAsState()
    val currentWeek by viewModel.currentWeek.collectAsState()
    val canGoBack by viewModel.canGoBack.collectAsState()
    val filteredClasses = remember(classes, selectedDate) {
        classes.filter { LocalDate.parse(it.date) == selectedDate }
    }
    val today = viewModel.today


    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(LocalPadding.current.small)
        ) {
            TopScheduleSection(selectedDate = selectedDate,
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
            classes = filteredClasses
        )
    }
}

@Composable
private fun ClassesListSection(
    classes: List<ClassModel>,
) {
    Box(modifier = Modifier.fillMaxWidth().padding(LocalPadding.current.tiny)) {
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
                    DanceClassCard(danceClass /* onReserveClick = {  Acción de reserva  }*/)
                }
            }
        }
    }
}

@Composable
private fun WeekSelectorSection(
    currentWeek: List<LocalDate>,
    selectedDate: LocalDate,
    today: LocalDate,
    canGoBack: Boolean,
    onDateSelected: (LocalDate) -> Unit,
    onPreviousWeek: () -> Unit,
    onNextWeek: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(8.dp), elevation = 4.dp, modifier = Modifier.fillMaxWidth()
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
private fun TopScheduleSection(
    selectedDate: LocalDate, today: LocalDate, onClickToday: () -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
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
        TextButton(
            onClick = {
                onClickToday()
            }, enabled = !isDisabled
        ) {
            Text(
                text = "Hoy",
                color = if (isDisabled) Color.Gray.copy(alpha = 0.5f) else Color.Blue,
                fontWeight = FontWeight.Bold
            )
        }
    }
}