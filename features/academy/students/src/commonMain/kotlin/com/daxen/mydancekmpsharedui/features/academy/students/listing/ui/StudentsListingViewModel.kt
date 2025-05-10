package com.daxen.mydancekmpsharedui.features.academy.students.listing.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StudentsListingViewModel : ViewModel() {
    private val _students = MutableStateFlow<List<StudentModel>>(emptyList())
    val students: StateFlow<List<StudentModel>> = _students.asStateFlow()

    init {
        loadStudents()
    }

    private fun loadStudents() {
        // TODO: Implementar la carga real de estudiantes desde la base de datos
        // Por ahora usamos datos de ejemplo
        viewModelScope.launch {
            _students.value = listOf(
                StudentModel(
                    id = "1",
                    name = "Juan",
                    surnames = "Pérez García",
                    email = "juan.perez@email.com"
                ),
                StudentModel(
                    id = "2",
                    name = "María",
                    surnames = "López Sánchez",
                    email = "maria.lopez@email.com"
                ),
                StudentModel(
                    id = "3",
                    name = "Carlos",
                    surnames = "Rodríguez Martín",
                    email = "carlos.rodriguez@email.com"
                ),
                StudentModel(
                    id = "4",
                    name = "Laura",
                    surnames = "Fernández Torres",
                    email = "laura.fernandez@email.com"
                ),
                StudentModel(
                    id = "5",
                    name = "Antonio",
                    surnames = "González Ruiz",
                    email = "antonio.gonzalez@email.com"
                ),
                StudentModel(
                    id = "6",
                    name = "Sofía",
                    surnames = "Martínez Ortega",
                    email = "sofia.martinez@email.com"
                ),
                StudentModel(
                    id = "7",
                    name = "Miguel",
                    surnames = "Sánchez Pérez",
                    email = "miguel.sanchez@email.com"
                ),
                StudentModel(
                    id = "8",
                    name = "Patricia",
                    surnames = "Díaz Jiménez",
                    email = "patricia.diaz@email.com"
                ),
                StudentModel(
                    id = "9",
                    name = "David",
                    surnames = "Moreno Castro",
                    email = "david.moreno@email.com"
                ),
                StudentModel(
                    id = "10",
                    name = "Lucía",
                    surnames = "Álvarez Vega",
                    email = "lucia.alvarez@email.com"
                )
            )
        }
    }
}