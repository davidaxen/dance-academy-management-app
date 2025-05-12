package com.daxen.mydancekmpsharedui.features.student.reservation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daxen.mydancekmpsharedui.data.classes.repository.ClassesRepository
import com.daxen.mydancekmpsharedui.data.reservation.repository.ReservationRepository
import com.daxen.mydancekmpsharedui.data.user.model.User
import com.daxen.mydancekmpsharedui.data.user.repository.UserRepository
import com.daxen.mydancekmpsharedui.features.student.reservation.utils.ClassOrigin
import com.daxen.mydancekmpsharedui.features.student.reservation.utils.DisplayClass
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn

internal class ReservationViewModel(
    userRepository: UserRepository,
    private val classesRepository: ClassesRepository,
    private val reservationRepository: ReservationRepository,
) : ViewModel() {
    val currentUser: StateFlow<User> = userRepository.currentUser

    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())

    private val _classesListState = MutableStateFlow<ClassesListUiState>(ClassesListUiState.Loading)
    val classesListState: StateFlow<ClassesListUiState> get() = _classesListState

    private val _selectedDate = MutableStateFlow(today)
    val selectedDate: StateFlow<LocalDate> = _selectedDate

    private val _currentWeek = MutableStateFlow(getCurrentWeek(today))
    val currentWeek: StateFlow<List<LocalDate>> = _currentWeek

    val canGoBack: StateFlow<Boolean> = _currentWeek.map { week ->
        week.last().minus(7, DateTimeUnit.DAY) >= today
    }.stateIn(viewModelScope, SharingStarted.Eagerly, true)

    // Almacén de profesores por ID
    private val teachersMap = MutableStateFlow<Map<String, String>>(emptyMap())

    init {
        loadClasses()
    }

    private fun loadClasses() {
        viewModelScope.launch {
            _classesListState.value = ClassesListUiState.Loading

            try {
                classesRepository.getClassesByAcademyId(currentUser.value.currentAcademyId)

                val weekly = classesRepository.weeklyClassesList.value
                val specific = classesRepository.specificClassesList.value

                // Aquí crearíamos un mapa de ID de profesor a nombre
                // Por ahora, asignamos nombres ficticios basados en los IDs
                val teacherIds = (weekly.map { it.data.teacherId } + specific.map { it.data.teacherId }).distinct()
                val teacherNames = teacherIds.associateWith { teacherId ->
                    "Profesor $teacherId" // En el futuro, obtendríamos el nombre real
                }
                teachersMap.value = teacherNames

                if (weekly.isEmpty() && specific.isEmpty()) {
                    _classesListState.value = ClassesListUiState.Empty
                } else {
                    _classesListState.value = ClassesListUiState.Success(
                        weekly = weekly,
                        specific = specific
                    )
                }
            } catch (e: Exception) {
                _classesListState.value = ClassesListUiState.Error
            }
        }
    }

    fun reserveClass(academyId: String, studentId: String, classId: String, name: String, hour: String) {
        viewModelScope.launch {
            try {
                reservationRepository.reserveClass(academyId, studentId, classId, name, hour, selectedDate.value.toString())
            } catch (e: Exception) {
                println("Error al reservar clase: ${e.message}")
            }
        }
    }

    fun goToToday() {
        _selectedDate.value = today
        _currentWeek.value = getCurrentWeek(today)
    }

    fun selectDate(dateSelected: LocalDate) {
        _selectedDate.value = dateSelected
    }

    fun goToPreviousWeek() {
        _currentWeek.value = getPreviousWeek(_currentWeek.value.first())
        if (_currentWeek.value.contains(today)) {
            _selectedDate.value = today
        } else {
            _selectedDate.value = _currentWeek.value.first()
        }
    }

    fun goToNextWeek() {
        _currentWeek.value = getNextWeek(_currentWeek.value.first())
        _selectedDate.value = _currentWeek.value.first()
    }

    private fun getCurrentWeek(date: LocalDate): List<LocalDate> {
        val todayWeekDay = date.dayOfWeek.isoDayNumber
        return (0..6).map { date.plus(it - todayWeekDay + 1, DateTimeUnit.DAY) }
    }

    private fun getPreviousWeek(date: LocalDate) = getCurrentWeek(date.minus(7, DateTimeUnit.DAY))
    private fun getNextWeek(date: LocalDate) = getCurrentWeek(date.plus(7, DateTimeUnit.DAY))

    fun reloadClasses() {
        _classesListState.value = ClassesListUiState.Loading
        loadClasses()
    }
    
    fun getTeacherName(teacherId: String): String {
        return teachersMap.value[teacherId] ?: "Profesor sin asignar"
    }
}