package com.daxen.mydancekmpsharedui.features.student.calendar.ui.models

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

data class ReservedClass(
    val id: String,
    val name: String,
    val date: LocalDate,
    val time: LocalTime,
    val teacher: String,
    val room: String
) 