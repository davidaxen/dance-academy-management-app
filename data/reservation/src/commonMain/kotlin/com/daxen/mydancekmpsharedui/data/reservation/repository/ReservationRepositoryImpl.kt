package com.daxen.mydancekmpsharedui.data.reservation.repository

import com.daxen.mydancekmpsharedui.core.firebase.reservations.FirebaseReservationService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ReservationRepositoryImpl(
    private val firebaseReservationService: FirebaseReservationService
): ReservationRepository {
    private val _daysWithReservationsList = MutableStateFlow<List<String>>(emptyList())
    override val daysWithReservationsList: StateFlow<List<String>> = _daysWithReservationsList

    override suspend fun reserveClass(academyId: String, studentId: String, classId: String, name: String, hour: String, selectedDate: String) {
        firebaseReservationService.reserveClass(academyId, studentId, classId, name, hour, selectedDate)
    }

    override suspend fun getReservationDates(userId: String) {
        val datesList = firebaseReservationService.getReservationDates(userId)
        _daysWithReservationsList.value = datesList
    }
}