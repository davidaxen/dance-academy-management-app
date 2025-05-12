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

class EditClassViewModel(
    private val academyClassesRepository: AcademyClassesRepository,
    private val academyTeachersRepository: AcademyTeachersRepository,
    academyUserRepository: AcademyUserRepository
) : ViewModel() {
    
    private val currentAcademy: StateFlow<UserAcademy> = academyUserRepository.currentAcademy
    
    private val _uiState = MutableStateFlow(EditClassUiState())
    val uiState: StateFlow<EditClassUiState> = _uiState.asStateFlow()
    
    private val _teacherSearchQuery = MutableStateFlow("")
    val teacherSearchQuery: StateFlow<String> = _teacherSearchQuery

    private val _availableTeachers: StateFlow<List<Teacher>> = academyTeachersRepository.teachers
    private val _filteredTeachers = MutableStateFlow<List<Teacher>>(emptyList())
    val filteredTeachers: StateFlow<List<Teacher>> = _filteredTeachers
    
    init {
        viewModelScope.launch {
            // Esperamos a tener una academia actual antes de cargar los profesores
            if (currentAcademy.value.academyId.isNotEmpty()) {
                loadTeachers()
            }
        }
    }
    
    fun loadClass(classId: String, isWeeklyClass: Boolean) {
        _uiState.update { it.copy(isLoading = true, error = "") }
        
        viewModelScope.launch {
            try {
                val academyId = currentAcademy.value.academyId
                
                // Cargar los profesores primero
                academyTeachersRepository.getTeachersByAcademyID(academyId)
                
                // Luego cargar la clase según su tipo
                if (isWeeklyClass) {
                    val weeklyClasses = academyClassesRepository.getWeeklyClassesByAcademyId(academyId)
                    val weeklyClass = weeklyClasses.find { it.id == classId }
                    
                    weeklyClass?.let { loadedClass ->
                        // Encontrar los profesores seleccionados en la lista de disponibles
                        val selectedTeachers = findSelectedTeachers(loadedClass.teachers, _availableTeachers.value)
                        
                        _uiState.update { 
                            it.copy(
                                isLoading = false,
                                selectedClassType = ClassType.WEEKLY,
                                classId = loadedClass.id,
                                name = loadedClass.name,
                                hour = loadedClass.hour,
                                dayOfWeek = loadedClass.dayOfWeek,
                                selectedTeachers = selectedTeachers,
                                status = loadedClass.status,
                                availableSpots = loadedClass.availableSpots.toString(),
                                isWeeklyClass = true
                            )
                        }
                    } ?: run {
                        _uiState.update { 
                            it.copy(
                                isLoading = false,
                                error = "No se encontró la clase"
                            )
                        }
                    }
                } else {
                    val specificClasses = academyClassesRepository.getSpecificClassesByAcademyId(academyId)
                    val specificClass = specificClasses.find { it.id == classId }
                    
                    specificClass?.let { loadedClass ->
                        // Encontrar los profesores seleccionados en la lista de disponibles
                        val selectedTeachers = findSelectedTeachers(loadedClass.teachers, _availableTeachers.value)
                        
                        _uiState.update { 
                            it.copy(
                                isLoading = false,
                                selectedClassType = ClassType.SPECIFIC,
                                classId = loadedClass.id,
                                name = loadedClass.name,
                                hour = loadedClass.hour,
                                date = loadedClass.date,
                                selectedTeachers = selectedTeachers,
                                status = loadedClass.status,
                                availableSpots = loadedClass.availableSpots.toString(),
                                isWeeklyClass = false
                            )
                        }
                    } ?: run {
                        _uiState.update { 
                            it.copy(
                                isLoading = false,
                                error = "No se encontró la clase"
                            )
                        }
                    }
                }
                
                filterTeachers()
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Error al cargar la clase"
                    )
                }
            }
        }
    }
    
    private fun findSelectedTeachers(teacherModels: List<TeacherModel>, availableTeachers: List<Teacher>): List<Teacher> {
        return teacherModels.mapNotNull { teacherModel ->
            availableTeachers.find { it.uid == teacherModel.id }
        }
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
    
    fun onStatusChanged(status: String) {
        _uiState.update { it.copy(status = status) }
    }
    
    fun onAvailableSpotsChanged(spots: String) {
        _uiState.update { it.copy(availableSpots = spots) }
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
        val query = _teacherSearchQuery.value.lowercase()
        val selectedTeacherIds = _uiState.value.selectedTeachers.map { it.uid }
        
        _filteredTeachers.value = if (query.isEmpty()) {
            _availableTeachers.value.filter { it.uid !in selectedTeacherIds }
        } else {
            _availableTeachers.value.filter { 
                it.uid !in selectedTeacherIds && (
                    it.name.lowercase().contains(query) || 
                    it.lastName.lowercase().contains(query)
                )
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

    fun updateClass() {
        if (!validateInput()) {
            _uiState.update { it.copy(showError = true) }
            return
        }

        _uiState.update { it.copy(isLoading = true, showError = false) }

        viewModelScope.launch {
            val state = _uiState.value
            
            // Convertir los profesores seleccionados al modelo esperado
            val teachers = state.selectedTeachers.map {
                TeacherModel(id = it.uid, name = "${it.name} ${it.lastName}")
            }
            
            val availableSpots = state.availableSpots.toIntOrNull() ?: 10
            
            val result = when (state.selectedClassType) {
                ClassType.WEEKLY -> {
                    val weeklyClass = WeeklyClassModel(
                        dayOfWeek = state.dayOfWeek,
                        id = state.classId,
                        name = state.name,
                        hour = state.hour,
                        status = state.status,
                        teachers = teachers,
                        availableSpots = availableSpots
                    )
                    academyClassesRepository.saveWeeklyClass(currentAcademy.value.academyId, weeklyClass)
                }
                ClassType.SPECIFIC -> {
                    val specificClass = SpecificClassModel(
                        date = state.date,
                        id = state.classId,
                        name = state.name,
                        hour = state.hour,
                        status = state.status,
                        teachers = teachers,
                        availableSpots = availableSpots
                    )
                    academyClassesRepository.saveSpecificClass(currentAcademy.value.academyId, specificClass)
                }
            }

            result.fold(
                onSuccess = {
                    _uiState.update { 
                        it.copy(
                            isLoading = false, 
                            classUpdated = true
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update { 
                        it.copy(
                            isLoading = false, 
                            error = error.message ?: "Error al actualizar la clase",
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
        val spotsValid = state.availableSpots.toIntOrNull() != null || state.availableSpots.isEmpty()
        
        val typeSpecificFieldsValid = when (state.selectedClassType) {
            ClassType.WEEKLY -> state.dayOfWeek.isNotEmpty()
            ClassType.SPECIFIC -> state.date.isNotEmpty()
        }
        
        return nameValid && hourValid && typeSpecificFieldsValid && teachersValid && spotsValid
    }
    
    private fun isValidHourFormat(hour: String): Boolean {
        if (hour.isEmpty()) return true // No validamos si está vacío
        
        // Regex para formato HH:MM con HH entre 00-23 y MM entre 00-59
        val hourRegex = Regex("^([01]?[0-9]|2[0-3]):([0-5][0-9])$")
        return hourRegex.matches(hour)
    }
}

data class EditClassUiState(
    val selectedClassType: ClassType = ClassType.WEEKLY,
    val classId: String = "",
    val name: String = "",
    val hour: String = "",
    val dayOfWeek: String = "",
    val date: String = "",
    val status: String = "available",
    val availableSpots: String = "10",
    val selectedTeachers: List<Teacher> = emptyList(),
    val isLoading: Boolean = true,
    val classUpdated: Boolean = false,
    val isWeeklyClass: Boolean = true,
    val error: String = "",
    val showError: Boolean = false,
    val isHourFormatValid: Boolean = true
) 