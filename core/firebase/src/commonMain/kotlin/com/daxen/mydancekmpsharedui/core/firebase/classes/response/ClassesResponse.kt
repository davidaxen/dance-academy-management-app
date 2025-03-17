package com.daxen.mydancekmpsharedui.core.firebase.classes.response

import kotlinx.serialization.Serializable

@Serializable
data class ClassesResponse(
    val academyId: String,
    val availableSpots: Int,
    val date: String,
    val hour: String,
    val maxCapacity: Int,
    val name: String,
    val status: String,
)