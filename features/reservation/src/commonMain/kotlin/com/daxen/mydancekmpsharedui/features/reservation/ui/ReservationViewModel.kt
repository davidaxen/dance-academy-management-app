package com.daxen.mydancekmpsharedui.features.reservation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daxen.mydancekmpsharedui.data.classes.model.ClassModel
import com.daxen.mydancekmpsharedui.data.classes.repository.ClassesRepository
import com.daxen.mydancekmpsharedui.data.user.model.User
import com.daxen.mydancekmpsharedui.data.user.repository.UserRepository
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
    private val classesRepository: ClassesRepository
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


    init {
        getClasses()
    }

    private fun getClasses() {
        viewModelScope.launch {
            classesRepository.getClassesByAcademyId("ACADEMYID1")

            try {
                classesRepository.classesList.collect { classList ->
                    when (classList) {
                        null -> {
                            _classesListState.value = ClassesListUiState.Loading
                        }
                        emptyList<ClassModel>() -> {
                            _classesListState.value = ClassesListUiState.Empty
                        }
                        else -> {
                            _classesListState.value = ClassesListUiState.Success(classList)
                        }
                    }
                }
            } catch (e: Exception) {
                _classesListState.value = ClassesListUiState.Error
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
}