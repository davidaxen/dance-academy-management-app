package com.daxen.mydancekmpsharedui.features.academy.classes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daxen.mydancekmpsharedui.data.academy.classes.models.SpecificClassModel
import com.daxen.mydancekmpsharedui.data.academy.classes.models.TeacherModel
import com.daxen.mydancekmpsharedui.data.academy.classes.models.WeeklyClassModel
import com.daxen.mydancekmpsharedui.data.academy.classes.repository.AcademyClassesRepository
import com.daxen.mydancekmpsharedui.data.teachers.model.Teacher
import com.daxen.mydancekmpsharedui.data.teachers.repository.AcademyTeachersRepository
import com.daxen.mydancekmpsharedui.data.user.model.UserAcademy
import com.daxen.mydancekmpsharedui.data.user.repository.AcademyUserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateClassViewModel(
    private val academyClassesRepository: AcademyClassesRepository,
    private val academyTeachersRepository: AcademyTeachersRepository,
    academyUserRepository: AcademyUserRepository
) : ViewModel() {
    private val currentAcademy: StateFlow<UserAcademy> = academyUserRepository.currentAcademy

    private val _uiState = MutableStateFlow(CreateClassUiState())
    val uiState: StateFlow<CreateClassUiState> = _uiState.asStateFlow()

    private val _availableTeachers: StateFlow<List<Teacher>> = academyTeachersRepository.teachers

    private val _teacherSearchQuery = MutableStateFlow("")
    val teacherSearchQuery: StateFlow<String> = _teacherSearchQuery

    private val _filteredTeachers = MutableStateFlow<List<Teacher>>(emptyList())
    val filteredTeachers: StateFlow<List<Teacher>> = _filteredTeachers

    init {
        _uiState.update { it.copy(isLoading = false) }
        loadTeachers()
    }

    private fun loadTeachers() {
        viewModelScope.launch {
            try {
                academyTeachersRepository.getTeachersByAcademyID(currentAcademy.value.academyId)
                filterTeachers()
            } catch (e: Exception) {
                // Manejar error si es necesario
            }
        }
    }

    fun onClassTypeSelected(classType: ClassType) {
        _uiState.update { it.copy(selectedClassType = classType) }
    }

    fun onNameChanged(name: String) {
        _uiState.update { it.copy(name = name) }
    }

    fun onHourChanged(hour: String) {
        _uiState.update { 
            it.copy(
                hour = hour,
                isHourFormatValid = isValidHourFormat(hour)
            )
        }
    }

    fun onDayOfWeekChanged(dayOfWeek: String) {
        _uiState.update { it.copy(dayOfWeek = dayOfWeek) }
    }

    fun onDateChanged(date: String) {
        _uiState.update { it.copy(date = date) }
    }

    fun onTeacherSearchQueryChanged(query: String) {
        _teacherSearchQuery.value = query
        filterTeachers()
    }

    private fun filterTeachers() {
        println(_availableTeachers.value)
        val query = _teacherSearchQuery.value.lowercase()
        val selectedTeacherIds = _uiState.value.selectedTeachers.map { it.uid }
        
        _filteredTeachers.value = if (query.isEmpty()) {
            _availableTeachers.value.filter { it.uid !in selectedTeacherIds }
        } else {
            _availableTeachers.value.filter { 
                it.uid !in selectedTeacherIds && it.name.lowercase().contains(query)
            }
        }
    }

    fun addTeacher(teacher: Teacher) {
        val currentTeachers = _uiState.value.selectedTeachers.toMutableList()
        
        // Máximo 2 profesores por clase
        if (currentTeachers.size < 2) {
            currentTeachers.add(teacher)
            _uiState.update { it.copy(selectedTeachers = currentTeachers) }
            filterTeachers()
        }
    }

    fun removeTeacher(teacherId: String) {
        val updatedTeachers = _uiState.value.selectedTeachers.filter { it.uid != teacherId }
        _uiState.update { it.copy(selectedTeachers = updatedTeachers) }
        filterTeachers()
    }

    fun resetForm() {
        _uiState.update { 
            it.copy(
                name = "",
                hour = "",
                dayOfWeek = "",
                date = "",
                selectedTeachers = emptyList(),
                classCreated = false,
                showError = false,
                error = "",
                isHourFormatValid = true
            )
        }
        _teacherSearchQuery.value = ""
        filterTeachers()
    }

    fun createClass() {
        if (!validateInput()) {
            _uiState.update { it.copy(showError = true) }
            return
        }

        _uiState.update { it.copy(isLoading = true, showError = false) }

        viewModelScope.launch {
            val state = _uiState.value
            
            // Convertir los profesores seleccionados al modelo esperado
            val teachers = state.selectedTeachers.map {
                TeacherModel(id = it.uid, name = it.name)
            }
            
            val result = when (state.selectedClassType) {
                ClassType.WEEKLY -> {
                    val weeklyClass = WeeklyClassModel(
                        dayOfWeek = state.dayOfWeek,
                        id = "",
                        name = state.name,
                        hour = state.hour,
                        status = "available",
                        teachers = teachers
                    )
                    academyClassesRepository.saveWeeklyClass(currentAcademy.value.academyId, weeklyClass)
                }
                ClassType.SPECIFIC -> {
                    val specificClass = SpecificClassModel(
                        date = state.date,
                        id = "",
                        name = state.name,
                        hour = state.hour,
                        status = "available",
                        teachers = teachers
                    )
                    academyClassesRepository.saveSpecificClass(currentAcademy.value.academyId, specificClass)
                }
            }

            result.fold(
                onSuccess = {
                    _uiState.update { 
                        it.copy(
                            isLoading = false, 
                            classCreated = true,
                            lastCreatedClassName = state.name
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update { 
                        it.copy(
                            isLoading = false, 
                            error = error.message ?: "Error al crear la clase",
                            showError = true
                        )
                    }
                }
            )
        }
    }

    private fun validateInput(): Boolean {
        val state = _uiState.value
        
        val nameValid = state.name.isNotEmpty()
        val hourValid = state.hour.isNotEmpty() && state.isHourFormatValid
        val teachersValid = state.selectedTeachers.isNotEmpty()
        
        val typeSpecificFieldsValid = when (state.selectedClassType) {
            ClassType.WEEKLY -> state.dayOfWeek.isNotEmpty()
            ClassType.SPECIFIC -> state.date.isNotEmpty()
        }
        
        return nameValid && hourValid && typeSpecificFieldsValid && teachersValid
    }
    
    private fun isValidHourFormat(hour: String): Boolean {
        if (hour.isEmpty()) return true // No validamos si está vacío
        
        // Regex para formato HH:MM con HH entre 00-23 y MM entre 00-59
        val hourRegex = Regex("^([01]?[0-9]|2[0-3]):([0-5][0-9])$")
        return hourRegex.matches(hour)
    }
}

data class CreateClassUiState(
    val selectedClassType: ClassType = ClassType.WEEKLY,
    val name: String = "",
    val hour: String = "",
    val dayOfWeek: String = "",
    val date: String = "",
    val selectedTeachers: List<Teacher> = emptyList(),
    val isLoading: Boolean = true,
    val classCreated: Boolean = false,
    val lastCreatedClassName: String = "",
    val error: String = "",
    val showError: Boolean = false,
    val isHourFormatValid: Boolean = true
)

enum class ClassType {
    WEEKLY, SPECIFIC
} 