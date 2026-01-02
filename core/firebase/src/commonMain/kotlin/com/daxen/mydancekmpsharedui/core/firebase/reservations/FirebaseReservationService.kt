package com.daxen.mydancekmpsharedui.core.firebase.reservations

import com.daxen.mydancekmpsharedui.core.firebase.reservations.models.ReservationModel

interface FirebaseReservationService {
    suspend fun reserveClass(academyId: String, studentId: String, classId: String, name: String, hour: String, selectedDate: String)
    suspend fun getReservationDates(userId: String, academyId: String): List<String>
    suspend fun getReservationsByDate(userId: String, academyId: String, date: String): List<ReservationModel>
    suspend fun cancelReservation(userId: String, academyId: String, classId: String, date: String)
}