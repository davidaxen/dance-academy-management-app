package com.daxen.mydancekmpsharedui.data.reservation.repository

interface ReservationRepository {
    suspend fun reserveClass(academyId: String, studentId: String, classId: String)
}