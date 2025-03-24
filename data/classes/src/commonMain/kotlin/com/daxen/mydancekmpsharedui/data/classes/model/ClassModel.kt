package com.daxen.mydancekmpsharedui.data.classes.model


data class ClassModel(
    val name: String,
    val hour: String,
    val status: String,
    val teacherId: String,
    val day: String,
    val availableSpots: Int = 10
)