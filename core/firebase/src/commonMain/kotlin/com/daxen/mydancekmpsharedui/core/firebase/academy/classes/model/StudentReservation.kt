package com.daxen.mydancekmpsharedui.core.firebase.academy.classes.model

import kotlinx.serialization.Serializable

@Serializable
data class StudentReservation(
    val studentInfo: StudentInfoReservation,
    val reservation: ReservationAcademyModel,
)
