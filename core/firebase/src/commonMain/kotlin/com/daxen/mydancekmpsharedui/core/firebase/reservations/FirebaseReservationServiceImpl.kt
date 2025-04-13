package com.daxen.mydancekmpsharedui.core.firebase.reservations

import com.daxen.mydancekmpsharedui.core.firebase.reservations.models.ReservationModel
import dev.gitlive.firebase.firestore.FirebaseFirestore

class FirebaseReservationServiceImpl(
    private val firestore: FirebaseFirestore,
//    private val auth: FirebaseAuth
): FirebaseReservationService {
    override suspend fun reserveClass(academyId: String, studentId: String, classId: String) {
        val reservation = ReservationModel(
            userId = studentId,
            academyId = academyId,
            classId = classId
        )
        firestore.collection("academies/$academyId/reservations").add(reservation)
    }
}