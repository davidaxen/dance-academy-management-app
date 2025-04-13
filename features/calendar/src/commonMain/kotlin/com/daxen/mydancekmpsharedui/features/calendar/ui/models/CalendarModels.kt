package com.daxen.mydancekmpsharedui.features.calendar.ui.models

import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month

data class CalendarMonth(
    val year: Int,
    val month: Month
) {
    val firstDayOfMonth: LocalDate
        get() = LocalDate(year, month, 1)

    val lastDayOfMonth: LocalDate
        get() = LocalDate(year, month, getDaysInMonth(month, year))

    private fun getDaysInMonth(month: Month, year: Int): Int {
        return when (month) {
            Month.FEBRUARY -> if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) 29 else 28
            Month.APRIL, Month.JUNE, Month.SEPTEMBER, Month.NOVEMBER -> 30
            else -> 31
        }
    }

    fun nextMonth(): CalendarMonth {
        return if (month == Month.DECEMBER) {
            CalendarMonth(year + 1, Month.JANUARY)
        } else {
            CalendarMonth(year, Month.entries[month.ordinal + 1])
        }
    }

    fun previousMonth(): CalendarMonth {
        return if (month == Month.JANUARY) {
            CalendarMonth(year - 1, Month.DECEMBER)
        } else {
            CalendarMonth(year, Month.entries[month.ordinal - 1])
        }
    }
} 