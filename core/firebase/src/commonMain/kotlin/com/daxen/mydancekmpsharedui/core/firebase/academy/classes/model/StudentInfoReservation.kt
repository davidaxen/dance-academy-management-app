package com.daxen.mydancekmpsharedui.core.firebase.academy.classes.model

import kotlinx.serialization.Serializable

@Serializable
data class StudentInfoReservation(
    val uid: String,
    val name: String,
    val lastName: String,
    val danceRole: String,
)
