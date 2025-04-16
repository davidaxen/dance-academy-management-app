package com.daxen.mydancekmpsharedui.data.reservation.repository

import kotlinx.coroutines.flow.StateFlow

interface ReservationRepository {
    val daysWithReservationsList: StateFlow<List<String>>
    suspend fun reserveClass(academyId: String, studentId: String, classId: String, name: String, hour: String, selectedDate: String)
    suspend fun getReservationDates(userId: String)
}