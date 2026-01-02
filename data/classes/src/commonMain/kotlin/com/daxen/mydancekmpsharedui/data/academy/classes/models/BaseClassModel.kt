package com.daxen.mydancekmpsharedui.data.academy.classes.models

open class BaseClassModel(
    open val id: String,
    open val name: String,
    open val hour: String,
    open val status: String,
    open val teachers: List<TeacherModel>,
    open val availableSpots: Int = 10
) 