package com.daxen.mydancekmpsharedui.data.classes.model


data class ClassModel(
    val name: String,
//    val instructorId: String,
    val academyId: String,
    val date: String,
    val hour: String,
    val maxCapacity: Int,
    val availableSpots: Int,
    val status: String
)