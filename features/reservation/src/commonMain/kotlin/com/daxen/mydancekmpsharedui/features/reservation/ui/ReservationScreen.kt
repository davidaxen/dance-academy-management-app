package com.daxen.mydancekmpsharedui.features.reservation.ui

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daxen.mydancekmpsharedui.core.ui.LocalPadding
import com.daxen.mydancekmpsharedui.data.classes.model.ClassModel
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import org.koin.compose.viewmodel.koinViewModel

val weekDaysShort = listOf("L", "M", "X", "J", "V", "S", "D")
val monthNames = listOf(
    "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio",
    "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
)

fun getCurrentWeek(date: LocalDate): List<LocalDate> {
    val todayWeekDay = date.dayOfWeek.isoDayNumber

    return (0..6).map { date.plus(it - todayWeekDay + 1, DateTimeUnit.DAY) }
}

fun getPreviousWeek(date: LocalDate) = getCurrentWeek(date.minus(7, DateTimeUnit.DAY))
fun getNextWeek(date: LocalDate) = getCurrentWeek(date.plus(7, DateTimeUnit.DAY))

@Composable
internal fun ReservationScreen(
    viewModel: ReservationViewModel = koinViewModel()
) {
    val classesState = viewModel.classesListState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
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
                ClassScheduleScreen(classes)
            }

            is ClassesListUiState.Error -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Error al cargar las clases",
                        color = Color.Red,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = {  }) {
                        Text("Reintentar")
                    }
                }
            }
        }

    }
}

@Composable
fun ClassScheduleScreen(classes: List<ClassModel>) {
    val today: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
//    val today: LocalDate = LocalDate(2025, 3, 19)
    var selectedDate by remember { mutableStateOf(today) }
    var currentWeek by remember { mutableStateOf(getCurrentWeek(today)) }

    Column(
        modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(LocalPadding.current.small)
        ){
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    // Nombre del Mes Actual
                    Text(
                        text = "${selectedDate.dayOfMonth} de ${monthNames[selectedDate.monthNumber - 1]} ${selectedDate.year}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }

                // Botón "Hoy"
                val isDisabled = selectedDate == today
                TextButton(
                    onClick = {
                        selectedDate = today
                        currentWeek = getCurrentWeek(today)
                    },
                    enabled = !isDisabled // Deshabilita cuando ya estamos en "Hoy"
                ) {
                    Text(
                        text = "Hoy",
                        color = if (isDisabled) Color.Gray.copy(alpha = 0.5f) else Color.Blue,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Selector de Semana
            Surface(
                shape = RoundedCornerShape(8.dp),
                elevation = 4.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                WeekSelector(
                    currentWeek = currentWeek,
                    selectedDate = selectedDate,
                    today = today,
                    onDateSelected = { selectedDate = it },
                    onPreviousWeek = {
                        currentWeek = getPreviousWeek(currentWeek.first())
                        if (currentWeek.contains(today)) {
                            selectedDate = today
                        } else {
                            selectedDate = currentWeek.first()
                        }
                    },
                    onNextWeek = {
                        currentWeek = getNextWeek(currentWeek.first())
                        selectedDate = currentWeek.first()
                    }
                )
            }

        }


        // Listado de Clases filtrado por el día seleccionado
        Box(modifier = Modifier.fillMaxWidth().padding(LocalPadding.current.tiny)) {
            val filteredClasses = classes.filter {
                LocalDate.parse(it.date) == selectedDate
            }

            if (filteredClasses.isEmpty()) {
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
                    items(filteredClasses) { danceClass ->
                        DanceClassCard(danceClass, /* onReserveClick = {  Acción de reserva  }*/)
                    }
                }
            }


        }
    }
}

@Composable
fun WeekSelector(
    currentWeek: List<LocalDate>,
    selectedDate: LocalDate,
    today: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onPreviousWeek: () -> Unit,
    onNextWeek: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val canGoBack = currentWeek.last().minus(7, DateTimeUnit.DAY) >= today
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
                        text = weekDaysShort[index], // Inicial en español
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


@Composable
fun DanceClassCard(
    danceClass: ClassModel,
//  onReserveClick: (DanceClass) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = LocalPadding.current.tiny)
            .clickable { /* Ir a detalles */ },
        shape = RoundedCornerShape(12.dp),
        elevation = 4.dp
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {

//            Image(
//                painter = painterResource(id = danceClass.imageRes),
//                contentDescription = null,
//                modifier = Modifier.size(60.dp).clip(CircleShape)
//            )
            val local = LocalDate.parse(danceClass.date)
            Column(modifier = Modifier.weight(1f).padding(start = 16.dp)) {
                Text(danceClass.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("Instructor: ${danceClass.academyId}", fontSize = 14.sp, color = Color.Gray)
                Text("Dia: ${local.dayOfWeek}", fontSize = 14.sp, color = Color.Gray)
                Text("Hora: ${danceClass.hour}", fontSize = 14.sp, color = Color.Gray)
            }

            Button(
                onClick = {
//                    onReserveClick(danceClass)
                },
                enabled = danceClass.availableSpots > 0,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (danceClass.availableSpots > 0) Color.Green else Color.Gray
                )
            ) {
                Text(if (danceClass.availableSpots > 0) "Reservar" else "Llena")
            }
        }
    }
}