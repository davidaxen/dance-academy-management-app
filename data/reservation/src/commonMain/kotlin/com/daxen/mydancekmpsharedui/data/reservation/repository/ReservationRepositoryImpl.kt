package com.daxen.mydancekmpsharedui.data.reservation.repository

import com.daxen.mydancekmpsharedui.core.firebase.classes.FirebaseClassesService
import com.daxen.mydancekmpsharedui.core.firebase.reservations.FirebaseReservationService
import com.daxen.mydancekmpsharedui.core.firebase.user.FirebaseUserService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ReservationRepositoryImpl(
    private val firebaseReservationService: FirebaseReservationService,
    private val firebaseUserService: FirebaseUserService,
    private val firebaseClassesService: FirebaseClassesService
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
        
        // Obtener todas las clases para buscar información de profesores
        val weeklyClasses = firebaseClassesService.getWeeklyClassesByAcademyId(academyId)
        val specificClasses = firebaseClassesService.getSpecificClassesByAcademyId(academyId)
        
        // Crear un mapa de ID de clase a profesor para búsqueda rápida
        val classIdToTeacherMap = mutableMapOf<String, String>()
        
        // Llenar mapa con clases semanales
        weeklyClasses.forEach { weeklyClass ->
            if (weeklyClass.teachers.isNotEmpty()) {
                classIdToTeacherMap[weeklyClass.id] = weeklyClass.teachers[0].name
            }
        }
        
        // Llenar mapa con clases específicas
        specificClasses.forEach { specificClass ->
            if (specificClass.teachers.isNotEmpty()) {
                classIdToTeacherMap[specificClass.id] = specificClass.teachers[0].name
            }
        }
        
        return reservations.map { reservation ->
            // Buscar el nombre del profesor usando el ID de la clase
            val teacherName = classIdToTeacherMap[reservation.classId] ?: "Profesor sin asignar"
            
            ReservationDetails(
                id = reservation.documentId,
                classId = reservation.classId,
                className = reservation.className,
                hour = reservation.hour,
                teacherName = teacherName,
                date = reservation.date
            )
        }
    }
    
    override suspend fun cancelReservation(userId: String, academyId: String, classId: String, date: String) {
        firebaseReservationService.cancelReservation(userId, academyId, classId, date)
        // Actualizar las fechas de reserva después de cancelar
        getReservationDates(userId, academyId)
    }
}