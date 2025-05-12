package com.daxen.mydancekmpsharedui.data.reservation.repository

import com.daxen.mydancekmpsharedui.core.firebase.reservations.FirebaseReservationService
import com.daxen.mydancekmpsharedui.core.firebase.user.FirebaseUserService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ReservationRepositoryImpl(
    private val firebaseReservationService: FirebaseReservationService,
    private val firebaseUserService: FirebaseUserService
): ReservationRepository {
    private val _daysWithReservationsList = MutableStateFlow<List<String>>(emptyList())
    override val daysWithReservationsList: StateFlow<List<String>> = _daysWithReservationsList

    override suspend fun reserveClass(academyId: String, studentId: String, classId: String, name: String, hour: String, selectedDate: String) {
        firebaseReservationService.reserveClass(academyId, studentId, classId, name, hour, selectedDate)
        getReservationDates(studentId, academyId)
    }

    override suspend fun getReservationDates(userId: String, academyId: String) {
        val datesList = firebaseReservationService.getReservationDates(userId, academyId)
        _daysWithReservationsList.value = datesList
    }
    
    override suspend fun getReservationsByDate(userId: String, academyId: String, date: String): List<ReservationDetails> {
        val reservations = firebaseReservationService.getReservationsByDate(userId, academyId, date)
        
        return reservations.map { reservation ->
            // Por ahora establecemos un nombre de profesor genérico ya que no tenemos acceso a la info del profesor
            // En una implementación más completa, aquí buscaríamos el nombre del profesor por su ID
            ReservationDetails(
                id = reservation.classId,
                classId = reservation.classId,
                className = reservation.className,
                hour = reservation.hour,
                teacherName = "Profesor asignado" // En el futuro, obtener el nombre real del profesor
            )
        }
    }
}