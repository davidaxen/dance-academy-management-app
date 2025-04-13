package com.daxen.mydancekmpsharedui.data.reservation.repository

import com.daxen.mydancekmpsharedui.core.firebase.reservations.FirebaseReservationService

class ReservationRepositoryImpl(
    private val firebaseReservationService: FirebaseReservationService
): ReservationRepository {
    override suspend fun reserveClass(academyId: String, studentId: String, classId: String) {
        firebaseReservationService.reserveClass(academyId, studentId, classId)
    }
}