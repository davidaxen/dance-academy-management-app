package com.daxen.mydancekmpsharedui.data.academy.classes.models

data class WeeklyClassModel(
    val dayOfWeek: String,
    override val id: String,
    override val name: String,
    override val hour: String,
    override val status: String,
    override val teachers: List<TeacherModel>,
    override val availableSpots: Int = 10
) : BaseClassModel(id, name, hour, status, teachers, availableSpots) 