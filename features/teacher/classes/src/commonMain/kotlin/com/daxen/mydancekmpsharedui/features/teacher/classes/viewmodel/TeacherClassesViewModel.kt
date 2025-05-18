package com.daxen.mydancekmpsharedui.features.teacher.classes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daxen.mydancekmpsharedui.data.academy.classes.repository.AcademyClassesRepository
import com.daxen.mydancekmpsharedui.data.user.model.UserAcademy
import com.daxen.mydancekmpsharedui.data.user.repository.AcademyUserRepository
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
    private val academyClassesRepository: AcademyClassesRepository,
    academyUserRepository: AcademyUserRepository
) : ViewModel() {
    private val currentAcademy: StateFlow<UserAcademy> = academyUserRepository.currentAcademy

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

                // Por ahora simulamos que no hay clases
                // En el futuro, este método obtendrá las clases del profesor según su ID
                _uiState.value = TeacherClassesUiState.Success(emptyList())
            } catch (e: Exception) {
                _uiState.value =
                    TeacherClassesUiState.Error(e.message ?: "Error al cargar las clases")
            }
        }
    }
}