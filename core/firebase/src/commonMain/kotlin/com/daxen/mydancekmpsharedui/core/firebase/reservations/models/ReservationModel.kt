package com.daxen.mydancekmpsharedui.core.firebase.reservations.models

import kotlinx.serialization.Serializable

@Serializable
data class ReservationModel(
    val userId: String,
    val academyId: String,
    val classId: String,
)