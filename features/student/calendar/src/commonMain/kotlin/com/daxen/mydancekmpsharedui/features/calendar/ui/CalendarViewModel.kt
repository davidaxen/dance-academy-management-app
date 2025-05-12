package com.daxen.mydancekmpsharedui.features.student.calendar.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daxen.mydancekmpsharedui.data.reservation.repository.ReservationRepository
import com.daxen.mydancekmpsharedui.data.user.model.User
import com.daxen.mydancekmpsharedui.data.user.repository.UserRepository
import com.daxen.mydancekmpsharedui.features.student.calendar.ui.models.CalendarMonth
import com.daxen.mydancekmpsharedui.features.student.calendar.ui.models.ReservedClass
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

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

    // Estado para las clases reservadas específicas del día
    sealed class ReservedClassesState {
        data object Loading : ReservedClassesState()
        data object Empty : ReservedClassesState()
        data class Success(val reservedClasses: List<ReservedClass>) : ReservedClassesState()
        data object Error : ReservedClassesState()
    }

    private val _reservedClassesState = MutableStateFlow<ReservedClassesState>(ReservedClassesState.Empty)
    val reservedClassesState: StateFlow<ReservedClassesState> = _reservedClassesState.asStateFlow()

    init {
        // Cargar los datos inicialmente
        loadDaysWithReservations()
    }

    fun loadDaysWithReservations() {
        viewModelScope.launch {
            try {
                _daysWithReservationsList.value = CalendarDatesListUiState.Loading
                repository.getReservationDates(currentUser.value.uid, currentUser.value.currentAcademyId)

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
                
                // Cargar las reservas para la fecha seleccionada
                loadReservationsForSelectedDate()
            } catch (e: Exception) {
                _daysWithReservationsList.value = CalendarDatesListUiState.Error
            }
        }
    }

    fun onDateSelected(date: LocalDate) {
        _selectedDate.value = date
        loadReservationsForSelectedDate()
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
                else -> false
            }
        }
    }
    
    // Carga las reservas para la fecha seleccionada
    fun loadReservationsForSelectedDate() {
        viewModelScope.launch {
            try {
                _reservedClassesState.value = ReservedClassesState.Loading
                val date = _selectedDate.value.toString()
                
                val reservations = repository.getReservationsByDate(
                    userId = currentUser.value.uid,
                    academyId = currentUser.value.currentAcademyId,
                    date = date
                )
                
                if (reservations.isEmpty()) {
                    _reservedClassesState.value = ReservedClassesState.Empty
                } else {
                    val reservedClasses = reservations.map { reservation ->
                        ReservedClass(
                            id = reservation.id,
                            name = reservation.className,
                            date = _selectedDate.value,
                            time = try {
                                LocalTime.parse(reservation.hour)
                            } catch (e: Exception) {
                                LocalTime(0, 0)
                            },
                            teacher = reservation.teacherName,
                            room = "Sala Principal" // Por defecto, ya que no tenemos esta información todavía
                        )
                    }
                    _reservedClassesState.value = ReservedClassesState.Success(reservedClasses)
                }
            } catch (e: Exception) {
                _reservedClassesState.value = ReservedClassesState.Error
            }
        }
    }
} 