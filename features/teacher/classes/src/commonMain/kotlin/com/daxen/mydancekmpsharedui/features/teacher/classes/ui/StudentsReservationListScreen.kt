package com.daxen.mydancekmpsharedui.features.teacher.classes.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.style.TextOverflow
import com.daxen.mydancekmpsharedui.core.ui.LocalPadding
import com.daxen.mydancekmpsharedui.core.ui.composables.ErrorComponent
import com.daxen.mydancekmpsharedui.core.ui.composables.LoadingComponent
import com.daxen.mydancekmpsharedui.data.teacher.classes.models.StudentReservation
import com.daxen.mydancekmpsharedui.features.teacher.classes.viewmodel.StudentsReservationListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentsReservationListScreen(
    viewModel: StudentsReservationListViewModel,
    classId: String,
    date: String,
    className: String,
    onBackClick: () -> Unit
) {
    val padding = LocalPadding.current
    val state by viewModel.state.collectAsState()

    LaunchedEffect(key1 = classId) {
        viewModel.getStudentReservations(classId, date)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = className, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                state.isLoading -> {
                    LoadingComponent(
                        text = "Obteniendo reservas...",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                state.error != null -> {
                    ErrorComponent(
                        message = state.error ?: "Error desconocido",
                        modifier = Modifier.align(Alignment.Center)
                    ) {
                        viewModel.getStudentReservations(classId, date)
                    }
                }
                state.students.isEmpty() -> {
                    EmptyStudentsComponent(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    StudentsList(
                        students = state.students,
                        contentPadding = PaddingValues(padding.normal)
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyStudentsComponent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "No hay estudiantes inscritos",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(LocalPadding.current.small))
        Text(
            text = "Aún no hay estudiantes que hayan reservado esta clase",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun StudentsList(
    students: List<StudentReservation>,
    contentPadding: PaddingValues
) {
    LazyColumn(
        contentPadding = contentPadding
    ) {
        items(students) { student ->
            StudentItem(student = student)
            HorizontalDivider()
        }
    }
}

@Composable
private fun StudentItem(student: StudentReservation) {
    val padding = LocalPadding.current
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = padding.small),
        elevation = CardDefaults.cardElevation(
            defaultElevation = padding.extraTiny
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding.normal)
        ) {
            Text(
                text = "${student.studentInfo.name} ${student.studentInfo.lastName}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(padding.tiny))
            
            Row {
                Text(
                    text = "Rol de baile:",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.padding(start = padding.tiny))
                Text(
                    text = student.studentInfo.danceRole,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            
            Spacer(modifier = Modifier.height(padding.tiny))
            
            Row {
                Text(
                    text = "Hora:",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.padding(start = padding.tiny))
                Text(
                    text = student.reservation.hour,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}