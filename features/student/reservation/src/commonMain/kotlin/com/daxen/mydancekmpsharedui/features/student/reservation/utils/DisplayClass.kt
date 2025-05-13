package com.daxen.mydancekmpsharedui.features.student.reservation.utils

data class DisplayClass(
    val id: String,
    val name: String,
    val hour: String,
    val status: String,
    val teacherId: String,
    val teacherName: String = "",
    val availableSpots: Int = 10,
//    val room: String,
    val origin: ClassOrigin
)

enum class ClassOrigin {
    WEEKLY, SPECIFIC
}