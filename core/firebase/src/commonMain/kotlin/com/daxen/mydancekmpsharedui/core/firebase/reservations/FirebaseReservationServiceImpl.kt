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
    
    override suspend fun getReservationsByDate(userId: String, academyId: String, date: String): List<ReservationModel> {
        val snapshot = firestore.collection("users/$userId/reservations")
            .where {
                all(
                    "academyId" equalTo academyId,
                    "date" equalTo date
                )
            }
            .get()
            
        return snapshot.documents.map { document ->
            // Añadir el ID del documento para poder identificarlo al cancelar
            val reservation = document.data(ReservationModel.serializer())
            reservation.copy(documentId = document.id)
        }
    }
    
    override suspend fun cancelReservation(userId: String, academyId: String, classId: String, date: String) {
        // Buscar la reserva específica para esta fecha y clase en las reservas del usuario
        val userReservationsSnapshot = firestore.collection("users/$userId/reservations")
            .where {
                all(
                    "academyId" equalTo academyId,
                    "classId" equalTo classId,
                    "date" equalTo date
                )
            }
            .get()
        
        // Si encontramos documentos, eliminarlos
        userReservationsSnapshot.documents.forEach { document ->
            // Eliminar de la colección del usuario
            firestore.collection("users/$userId/reservations").document(document.id).delete()
            
            // Buscar y eliminar también de la colección de la academia
            val academyReservationsSnapshot = firestore.collection("academies/$academyId/reservations")
                .where {
                    all(
                        "userId" equalTo userId,
                        "classId" equalTo classId,
                        "date" equalTo date
                    )
                }
                .get()
            
            academyReservationsSnapshot.documents.forEach { academyDocument ->
                firestore.collection("academies/$academyId/reservations").document(academyDocument.id).delete()
            }
        }
    }
}