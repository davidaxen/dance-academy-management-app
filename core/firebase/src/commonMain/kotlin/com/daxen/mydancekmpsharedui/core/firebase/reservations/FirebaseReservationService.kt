package com.daxen.mydancekmpsharedui.core.firebase.reservations

interface FirebaseReservationService {
    suspend fun reserveClass(academyId: String, studentId: String, classId: String, name: String, hour: String, selectedDate: String)
    suspend fun getReservationDates(userId: String, academyId: String): List<String>
}