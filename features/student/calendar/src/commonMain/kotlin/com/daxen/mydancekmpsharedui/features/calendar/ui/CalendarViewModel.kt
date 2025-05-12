package com.daxen.mydancekmpsharedui.features.student.calendar.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daxen.mydancekmpsharedui.data.reservation.repository.ReservationRepository
import com.daxen.mydancekmpsharedui.data.user.model.User
import com.daxen.mydancekmpsharedui.data.user.repository.UserRepository
import com.daxen.mydancekmpsharedui.features.student.calendar.ui.models.CalendarMonth
import com.daxen.mydancekmpsharedui.features.student.calendar.ui.models.ReservedClass
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import kotlinx.datetime.TimeZone

class CalendarViewModel(
    userRepository: UserRepository,
    private val repository: ReservationRepository
) : ViewModel() {
    private val currentUser: StateFlow<User> = userRepository.currentUser
    private val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

    private val _selectedDate = MutableStateFlow(currentDate)
    val selectedDate: StateFlow<LocalDate> = _selectedDate.asStateFlow()

    private val _currentMonth = MutableStateFlow(CalendarMonth(currentDate.year, currentDate.month))
    val currentMonth: StateFlow<CalendarMonth> = _currentMonth.asStateFlow()

    private val _daysWithReservationsList = MutableStateFlow<CalendarDatesListUiState>(CalendarDatesListUiState.Loading)
    val daysWithReservationsList: StateFlow<CalendarDatesListUiState> = _daysWithReservationsList.asStateFlow()

    // Datos simulados - En el futuro esto vendrá del repositorio
    private val _reservedClasses = MutableStateFlow(
        listOf(
            ReservedClass(
                id = "1",
                name = "Bachata Intermedio",
                date = currentDate,
                time = LocalTime(20, 0),
                teacher = "Lucía Gómez",
                room = "Sala 1"
            ),
            ReservedClass(
                id = "2",
                name = "Salsa Avanzado",
                date = currentDate,
                time = LocalTime(21, 30),
                teacher = "Carlos Pérez",
                room = "Sala 2"
            )
        )
    )
    val reservedClasses: StateFlow<List<ReservedClass>> = _reservedClasses.asStateFlow()

    fun loadDaysWithReservations() {
        viewModelScope.launch {
            try {
                if (repository.daysWithReservationsList.value.isEmpty()) {
                    _daysWithReservationsList.value = CalendarDatesListUiState.Loading
                    repository.getReservationDates(currentUser.value.uid, currentUser.value.currentAcademyId)
                }

                val dateStrings = repository.daysWithReservationsList.value

                if (dateStrings.isEmpty()) {
                    _daysWithReservationsList.value = CalendarDatesListUiState.Empty
                } else {
                    _daysWithReservationsList.value = CalendarDatesListUiState.Success(
                        dateStrings.map { dateString ->
                            try {
                                LocalDate.parse(dateString)
                            } catch (e: Exception) {
                                throw IllegalArgumentException("Invalid date format: $dateString", e)
                            }
                        }
                    )
                }
            } catch (e: Exception) {
                _daysWithReservationsList.value = CalendarDatesListUiState.Error
            }
        }
    }

    fun onDateSelected(date: LocalDate) {
        _selectedDate.value = date
    }

    fun onPreviousMonthClick() {
        _currentMonth.value = _currentMonth.value.previousMonth()
    }

    fun onNextMonthClick() {
        _currentMonth.value = _currentMonth.value.nextMonth()
    }

    fun hasReservations(date: LocalDate): Boolean {
        _daysWithReservationsList.value.let { state ->
            return when (state) {
                is CalendarDatesListUiState.Success -> state.reservationsDatesList.contains(date)
                is CalendarDatesListUiState.Error, CalendarDatesListUiState.Loading, CalendarDatesListUiState.Empty -> false
            }
        }
    }

    fun getReservedClassesForDate(date: LocalDate): List<ReservedClass> {
        return _reservedClasses.value.filter { it.date == date }
    }
} 