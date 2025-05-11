package com.daxen.mydancekmpsharedui.features.academy.classes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daxen.mydancekmpsharedui.data.academy.classes.repository.AcademyClassesRepository
import com.daxen.mydancekmpsharedui.data.user.model.UserAcademy
import com.daxen.mydancekmpsharedui.data.user.repository.AcademyUserRepository
import com.daxen.mydancekmpsharedui.features.academy.classes.ui.composables.ClassesGroup
import com.daxen.mydancekmpsharedui.features.academy.classes.ui.models.AcademyClassesUiState
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

class AcademyClassesViewModel(
    private val academyClassesRepository: AcademyClassesRepository,
    academyUserRepository: AcademyUserRepository
) : ViewModel() {
    private val currentAcademy: StateFlow<UserAcademy> = academyUserRepository.currentAcademy

    private val _uiState = MutableStateFlow<AcademyClassesUiState>(AcademyClassesUiState.Loading)
    val uiState: StateFlow<AcademyClassesUiState> = _uiState

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    private val _selectedWeekStartDate = MutableStateFlow<LocalDate>(getCurrentWeekMonday())
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
                _uiState.value = AcademyClassesUiState.Loading

                // Obtenemos clases del repositorio
                var weeklyClasses =
                    academyClassesRepository.getWeeklyClassesByAcademyId(currentAcademy.value.academyId)
                var specificClasses =
                    academyClassesRepository.getSpecificClassesByAcademyId(currentAcademy.value.academyId)

                // Si no hay clases, añadimos datos de prueba
                if (weeklyClasses.isEmpty() && specificClasses.isEmpty()) {
                    val mockClasses = createMockClasses()
                    weeklyClasses = mockClasses.first
                    specificClasses = mockClasses.second
                }

                // Filtrar las clases semanales para el día seleccionado
                val selectedDayOfWeek = when (_selectedDate.value.dayOfWeek) {
                    DayOfWeek.MONDAY -> "MONDAY"
                    DayOfWeek.TUESDAY -> "TUESDAY"
                    DayOfWeek.WEDNESDAY -> "WEDNESDAY"
                    DayOfWeek.THURSDAY -> "THURSDAY"
                    DayOfWeek.FRIDAY -> "FRIDAY"
                    DayOfWeek.SATURDAY -> "SATURDAY"
                    DayOfWeek.SUNDAY -> "SUNDAY"
                    else -> "MONDAY"
                }

                val dayWeeklyClasses = weeklyClasses.filter { it.dayOfWeek == selectedDayOfWeek }

                // Filtrar las clases específicas para la fecha seleccionada exacta
                val daySpecificClasses = specificClasses.filter { specificClass ->
                    try {
                        // Convertir la fecha del string "YYYY-MM-DD" a LocalDate
                        val parts = specificClass.date.split("-")
                        if (parts.size == 3) {
                            val classDate = LocalDate(parts[0].toInt(), parts[1].toInt(), parts[2].toInt())

                            // Verificar si la fecha coincide exactamente con la seleccionada
                            classDate.year == _selectedDate.value.year &&
                            classDate.month == _selectedDate.value.month &&
                            classDate.dayOfMonth == _selectedDate.value.dayOfMonth
                        } else false
                    } catch (e: Exception) {
                        false
                    }
                }

                // Crear un único grupo para el día seleccionado
                val classesGroup = ClassesGroup(
                    dayOfWeek = _selectedDate.value.dayOfWeek,
                    weeklyClasses = dayWeeklyClasses,
                    specificClasses = daySpecificClasses
                )

                _uiState.value = AcademyClassesUiState.Success(listOf(classesGroup))
            } catch (e: Exception) {
                _uiState.value =
                    AcademyClassesUiState.Error(e.message ?: "Error al cargar las clases")
            }
        }
    }

    private fun createMockClasses(): Pair<List<com.daxen.mydancekmpsharedui.data.academy.classes.models.WeeklyClassModel>, List<com.daxen.mydancekmpsharedui.data.academy.classes.models.SpecificClassModel>> {
        // Crear profesores de prueba
        val teacher1 = com.daxen.mydancekmpsharedui.data.academy.classes.models.TeacherModel(
            id = "teacher1",
            name = "Carlos Rodríguez"
        )

        val teacher2 = com.daxen.mydancekmpsharedui.data.academy.classes.models.TeacherModel(
            id = "teacher2",
            name = "Laura Gómez"
        )

        // Crear clase semanal para los lunes
        val weeklyClasses = listOf(
            com.daxen.mydancekmpsharedui.data.academy.classes.models.WeeklyClassModel(
                id = "weekly1",
                name = "Bachata Intermedio",
                dayOfWeek = "MONDAY",
                hour = "19:00",
                teachers = listOf(teacher1),
                status = "available"
            ),
            com.daxen.mydancekmpsharedui.data.academy.classes.models.WeeklyClassModel(
                id = "weekly2",
                name = "Salsa Avanzada",
                dayOfWeek = "MONDAY",
                hour = "20:30",
                teachers = listOf(teacher1, teacher2),
                status = "waitlist"
            )
        )

        // Crear clase específica para el 12 de mayo de 2025
        val specificClasses = listOf(
            com.daxen.mydancekmpsharedui.data.academy.classes.models.SpecificClassModel(
                id = "specific1",
                name = "Taller Especial de Bachata Sensual",
                date = "2025-05-12", // 12 de mayo de 2025
                hour = "18:00",
                teachers = listOf(teacher2),
                status = "available"
            )
        )

        return Pair(weeklyClasses, specificClasses)
    }
} 