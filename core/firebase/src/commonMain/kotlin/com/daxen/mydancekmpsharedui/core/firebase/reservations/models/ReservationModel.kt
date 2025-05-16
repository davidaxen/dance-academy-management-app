package com.daxen.mydancekmpsharedui.core.firebase.reservations.models

import kotlinx.serialization.Serializable

@Serializable
data class ReservationModel(
    val userId: String,
    val classId: String,
    val date: String,
    val className: String,
    val academyId: String,
    val hour: String,
    val documentId: String = "" // ID del documento en Firestore
)