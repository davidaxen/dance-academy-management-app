package com.daxen.mydancekmpsharedui.core.firebase.reservations

import com.daxen.mydancekmpsharedui.core.firebase.reservations.models.ClassDateModel
import com.daxen.mydancekmpsharedui.core.firebase.reservations.models.ReservationModel
import dev.gitlive.firebase.firestore.FirebaseFirestore

class FirebaseReservationServiceImpl(
    private val firestore: FirebaseFirestore
): FirebaseReservationService {
    override suspend fun reserveClass(academyId: String, studentId: String, classId: String, name: String, hour: String, selectedDate: String) {
        val reservation = ReservationModel(
            userId = studentId,
            classId = classId,
            date = selectedDate,
            className = name,
            academyId = academyId,
            hour = hour
        )
        firestore.collection("academies/$academyId/reservations").add(reservation)
        firestore.collection("users/$studentId/reservations").add(reservation)
    }

    override suspend fun getReservationDates(userId: String, academyId: String): List<String> {
        val snapshot = firestore.collection("users/$userId/reservations")
            .where {
                "academyId" equalTo academyId
            }
            .get()

        return snapshot.documents.map { document ->
            document.data(ClassDateModel.serializer()).date
        }
    }
}