package com.daxen.mydancekmpsharedui.features.calendar.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.daxen.mydancekmpsharedui.features.calendar.ui.models.CalendarMonth
import com.daxen.mydancekmpsharedui.features.calendar.ui.models.ReservedClass
import kotlinx.coroutines.flow.*
import kotlinx.datetime.*
import kotlinx.datetime.TimeZone

class CalendarViewModel : ViewModel() {
    private val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

    private val _selectedDate = MutableStateFlow(currentDate)
    val selectedDate: StateFlow<LocalDate> = _selectedDate.asStateFlow()

    private val _currentMonth = MutableStateFlow(CalendarMonth(currentDate.year, currentDate.month))
    val currentMonth: StateFlow<CalendarMonth> = _currentMonth.asStateFlow()

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
        return _reservedClasses.value.any { it.date == date }
    }

    fun getReservedClassesForDate(date: LocalDate): List<ReservedClass> {
        return _reservedClasses.value.filter { it.date == date }
    }
} 