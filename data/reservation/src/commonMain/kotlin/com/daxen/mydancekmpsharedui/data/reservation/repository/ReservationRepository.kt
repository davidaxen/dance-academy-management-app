package com.daxen.mydancekmpsharedui.data.reservation.repository

import kotlinx.coroutines.flow.StateFlow

interface ReservationRepository {
    val daysWithReservationsList: StateFlow<List<String>>
    suspend fun reserveClass(academyId: String, studentId: String, classId: String, name: String, hour: String, selectedDate: String)
    suspend fun getReservationDates(userId: String, academyId: String)
    suspend fun getReservationsByDate(userId: String, academyId: String, date: String): List<ReservationDetails>
}

data class ReservationDetails(
    val id: String,
    val classId: String,
    val className: String,
    val hour: String,
    val teacherName: String
)