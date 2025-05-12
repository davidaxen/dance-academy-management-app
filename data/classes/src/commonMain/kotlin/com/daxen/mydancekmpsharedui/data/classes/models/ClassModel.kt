package com.daxen.mydancekmpsharedui.data.classes.models

data class ClassModel(
    val id: String,
    val name: String,
    val hour: String,
    val status: String,
    val teacherId: String = "",
    val availableSpots: Int = 10,
    val teachers: List<TeacherInfo> = emptyList()
)

data class TeacherInfo(
    val id: String,
    val name: String
)