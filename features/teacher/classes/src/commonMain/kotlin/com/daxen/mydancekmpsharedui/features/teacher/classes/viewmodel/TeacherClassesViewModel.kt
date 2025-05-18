package com.daxen.mydancekmpsharedui.features.teacher.classes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daxen.mydancekmpsharedui.data.teacher.classes.repository.TeacherClassesRepository
import com.daxen.mydancekmpsharedui.data.user.model.User
import com.daxen.mydancekmpsharedui.data.user.repository.UserRepository
import com.daxen.mydancekmpsharedui.features.teacher.classes.ui.composables.ClassesGroup
import com.daxen.mydancekmpsharedui.features.teacher.classes.ui.models.TeacherClassesUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn

class TeacherClassesViewModel(
    private val teacherClassesRepository: TeacherClassesRepository,
    userRepository: UserRepository,
) : ViewModel() {
    private val currentUser: StateFlow<User> = userRepository.currentUser

    private val _uiState = MutableStateFlow<TeacherClassesUiState>(TeacherClassesUiState.Loading)
    val uiState: StateFlow<TeacherClassesUiState> = _uiState

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    private val _selectedWeekStartDate = MutableStateFlow(getCurrentWeekMonday())
    val selectedWeekStartDate: StateFlow<LocalDate> = _selectedWeekStartDate

    private val _selectedDate =
        MutableStateFlow(Clock.System.todayIn(TimeZone.currentSystemDefault()))
    val selectedDate: StateFlow<LocalDate> = _selectedDate

    init {
        _selectedWeekStartDate.value = getCurrentWeekMonday()
        _selectedDate.value = Clock.System.todayIn(TimeZone.currentSystemDefault())
        loadClasses()
    }

    private fun getCurrentWeekMonday(): LocalDate {
        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
        val mondayOffset = when (today.dayOfWeek) {
            DayOfWeek.MONDAY -> 0
            DayOfWeek.TUESDAY -> 1
            DayOfWeek.WEDNESDAY -> 2
            DayOfWeek.THURSDAY -> 3
            DayOfWeek.FRIDAY -> 4
            DayOfWeek.SATURDAY -> 5
            DayOfWeek.SUNDAY -> 6
            else -> 0
        }

        return today.minus(mondayOffset, DateTimeUnit.DAY)
    }

    fun moveWeekForward() {
        val newStartDate = _selectedWeekStartDate.value.plus(7, DateTimeUnit.DAY)
        _selectedWeekStartDate.value = newStartDate
        _selectedDate.value = newStartDate
        loadClasses()
    }

    fun moveWeekBackward() {
        val newStartDate = _selectedWeekStartDate.value.minus(7, DateTimeUnit.DAY)
        _selectedWeekStartDate.value = newStartDate
        _selectedDate.value = newStartDate
        loadClasses()
    }

    fun onDateSelected(date: LocalDate) {
        _selectedDate.value = date
        loadClasses()
    }

    fun refreshClasses() {
        viewModelScope.launch {
            _isRefreshing.value = true
            loadClasses()
            _isRefreshing.value = false
        }
    }

    private fun loadClasses() {
        viewModelScope.launch {
            try {
                _uiState.value = TeacherClassesUiState.Loading
                
                // Obtener las clases del profesor para el día seleccionado
                val weeklyClasses = teacherClassesRepository.getWeeklyClassesByTeacherAndAcademyId(
                    teacherId = currentUser.value.uid,
                    academyId = currentUser.value.currentAcademyId,
                    date = _selectedDate.value
                )
                
                val specificClasses = teacherClassesRepository.getSpecificClassesByTeacherAndAcademyId(
                    teacherId = currentUser.value.uid,
                    academyId = currentUser.value.currentAcademyId,
                    date = _selectedDate.value
                )
                
                // Crear un grupo para el día seleccionado
                val classesGroup = ClassesGroup(
                    dayOfWeek = _selectedDate.value.dayOfWeek,
                    weeklyClasses = weeklyClasses,
                    specificClasses = specificClasses
                )
                
                // Verificar si hay clases
                if (weeklyClasses.isEmpty() && specificClasses.isEmpty()) {
                    _uiState.value = TeacherClassesUiState.Empty(isFiltered = false)
                } else {
                    _uiState.value = TeacherClassesUiState.Success(listOf(classesGroup))
                }
            } catch (e: Exception) {
                _uiState.value =
                    TeacherClassesUiState.Error(e.message ?: "Error al cargar las clases")
            }
        }
    }
}