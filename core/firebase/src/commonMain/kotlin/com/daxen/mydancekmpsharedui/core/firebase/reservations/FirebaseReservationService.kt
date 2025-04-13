package com.daxen.mydancekmpsharedui.core.firebase.reservations

interface FirebaseReservationService {
    suspend fun reserveClass(academyId: String, studentId: String, classId: String)
}