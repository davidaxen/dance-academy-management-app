package com.daxen.mydancekmpsharedui.data.students.repository

import com.daxen.mydancekmpsharedui.core.firebase.academy.students.FirebaseAcademyStudentsRepository
import com.daxen.mydancekmpsharedui.data.students.model.Invitation
import com.daxen.mydancekmpsharedui.data.students.model.InvitationStatus
import com.daxen.mydancekmpsharedui.data.students.model.Student
import com.daxen.mydancekmpsharedui.data.students.model.mapper.toFirebaseModel
import com.daxen.mydancekmpsharedui.data.students.model.mapper.toInvitation
import com.daxen.mydancekmpsharedui.data.students.model.mapper.toStudent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AcademyStudentsRepositoryImpl(
    private val firebaseAcademyStudentsRepository: FirebaseAcademyStudentsRepository
): AcademyStudentsRepository {
    private val _students: MutableStateFlow<List<Student>> = MutableStateFlow(emptyList())
    override val students: StateFlow<List<Student>> = _students
    
    private val _invitations: MutableStateFlow<List<Invitation>> = MutableStateFlow(emptyList())
    override val invitations: StateFlow<List<Invitation>> = _invitations

    override suspend fun getStudentsByAcademyID(id: String) {
        val studentsResponse = firebaseAcademyStudentsRepository.getStudentsByAcademyID(id)
        _students.value = studentsResponse.map { it.toStudent() }
    }
    
    override suspend fun getInvitationsByAcademyID(id: String) {
        val invitationsResponse = firebaseAcademyStudentsRepository.getInvitationsByAcademyID(id)
        _invitations.value = invitationsResponse.map { it.toInvitation() }
    }

    override suspend fun inviteStudentToAcademy(invitation: Invitation): Boolean {
        val invitationModel = invitation.toFirebaseModel()
        return firebaseAcademyStudentsRepository.inviteStudentToAcademy(invitationModel)
    }
    
    override suspend fun deleteInvitation(invitationId: String): Boolean {
        val success = firebaseAcademyStudentsRepository.deleteInvitation(invitationId)
        
        // Si se eliminó con éxito, actualizamos la lista local
        if (success) {
            val updatedList = _invitations.value.filter { it.id != invitationId }
            _invitations.value = updatedList
        }
        
        return success
    }
    
    override suspend fun checkUserExists(email: String): Boolean {
        return firebaseAcademyStudentsRepository.checkUserExists(email)
    }
    
    override suspend fun getUserInvitations(email: String): List<Invitation> {
        val invitationsResponse = firebaseAcademyStudentsRepository.getUserInvitations(email)
        return invitationsResponse.map { it.toInvitation() }
    }
    
    override suspend fun canCreateInvitation(email: String): Pair<Boolean, String> {
        // Primero verificar si el usuario existe
        val userExists = checkUserExists(email)
        if (!userExists) {
            return Pair(false, "El correo electrónico no está registrado en el sistema")
        }
        
        // Verificar invitaciones existentes
        val userInvitations = getUserInvitations(email)
        if (userInvitations.isEmpty()) {
            return Pair(true, "")
        }
        
        // Verificar si hay alguna invitación aceptada o pendiente
        val hasActiveInvitation = userInvitations.any { 
            it.status == InvitationStatus.ACCEPTED || it.status == InvitationStatus.PENDING 
        }
        
        return if (hasActiveInvitation) {
            Pair(false, "El usuario ya tiene una invitación pendiente o aceptada")
        } else {
            Pair(true, "")
        }
    }
}