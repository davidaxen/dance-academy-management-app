package com.daxen.mydancekmpsharedui.features.student.calendar.ui

import kotlinx.datetime.LocalDate

sealed class CalendarDatesListUiState {
    data object Loading : CalendarDatesListUiState()
    data object Empty : CalendarDatesListUiState()
    data object Error : CalendarDatesListUiState()
    data class Success(val reservationsDatesList: List<LocalDate>) : CalendarDatesListUiState()
}