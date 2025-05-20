package com.daxen.mydancekmpsharedui.features.teacher.classes.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.daxen.mydancekmpsharedui.core.ui.LocalPadding
import com.daxen.mydancekmpsharedui.core.ui.composables.ErrorComponent
import com.daxen.mydancekmpsharedui.core.ui.composables.LoadingComponent
import com.daxen.mydancekmpsharedui.data.teacher.classes.models.StudentReservation
import com.daxen.mydancekmpsharedui.features.teacher.classes.viewmodel.DanceRoleFilter
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
    var isSearching by remember { mutableStateOf(false) }
    var showFilterDropdown by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = classId) {
        viewModel.getStudentReservations(classId, date)
    }

    Scaffold(
        topBar = {
            if (isSearching) {
                TopAppBar(
                    title = {
                        TextField(
                            value = state.searchQuery,
                            onValueChange = viewModel::onSearchQueryChanged,
                            placeholder = { Text("Buscar estudiante...") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { isSearching = false }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver"
                            )
                        }
                    },
                    actions = {
                        if (state.searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.onSearchQueryChanged("") }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Limpiar búsqueda"
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                        actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            } else {
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
                    actions = {
                        IconButton(onClick = { isSearching = true }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Buscar"
                            )
                        }
                        
                        Box {
                            IconButton(onClick = { showFilterDropdown = true }) {
                                BadgedBox(
                                    badge = {
                                        if (state.danceRoleFilter != DanceRoleFilter.ALL) {
                                            Badge {}
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.FilterList,
                                        contentDescription = "Filtrar por rol"
                                    )
                                }
                            }
                            
                            DropdownMenu(
                                expanded = showFilterDropdown,
                                onDismissRequest = { showFilterDropdown = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Todos") },
                                    onClick = {
                                        viewModel.onDanceRoleFilterChanged(DanceRoleFilter.ALL)
                                        showFilterDropdown = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Leader") },
                                    onClick = {
                                        viewModel.onDanceRoleFilterChanged(DanceRoleFilter.LEADER)
                                        showFilterDropdown = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Follower") },
                                    onClick = {
                                        viewModel.onDanceRoleFilterChanged(DanceRoleFilter.FOLLOWER)
                                        showFilterDropdown = false
                                    }
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                        actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
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
                    Surface(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        StudentsList(
                            students = state.students,
                            contentPadding = PaddingValues(padding.normal)
                        )
                    }
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
    val initials = getInitials(
        firstName = student.studentInfo.name,
        lastName = student.studentInfo.lastName
    )
    
    ListItem(
        headlineContent = { 
            Text(
                text = "${student.studentInfo.name} ${student.studentInfo.lastName}",
                fontWeight = FontWeight.Medium
            )
        },
        supportingContent = {
            Text(
                text = "Rol: ${student.studentInfo.danceRole}",
                style = MaterialTheme.typography.bodyMedium
            )
        },
        leadingContent = {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = initials,
                    color = MaterialTheme.colorScheme.onPrimary,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    )
}

private fun getInitials(firstName: String, lastName: String): String {
    val firstInitial = firstName.firstOrNull()?.uppercase() ?: ""
    val lastInitial = lastName.firstOrNull()?.uppercase() ?: ""
    return "$firstInitial$lastInitial"
}