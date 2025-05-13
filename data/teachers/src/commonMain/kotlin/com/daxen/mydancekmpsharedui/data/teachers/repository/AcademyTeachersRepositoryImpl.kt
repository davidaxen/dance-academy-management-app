package com.daxen.mydancekmpsharedui.data.teachers.repository

import com.daxen.mydancekmpsharedui.core.firebase.academy.teachers.FirebaseAcademyTeachersRepository
import com.daxen.mydancekmpsharedui.data.teachers.model.Invitation
import com.daxen.mydancekmpsharedui.data.teachers.model.InvitationStatus
import com.daxen.mydancekmpsharedui.data.teachers.model.Teacher
import com.daxen.mydancekmpsharedui.data.teachers.model.mapper.toFirebaseModel
import com.daxen.mydancekmpsharedui.data.teachers.model.mapper.toInvitation
import com.daxen.mydancekmpsharedui.data.teachers.model.mapper.toTeacher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AcademyTeachersRepositoryImpl(
    private val firebaseAcademyTeachersRepository: FirebaseAcademyTeachersRepository
) : AcademyTeachersRepository {
    
    private val _teachers = MutableStateFlow<List<Teacher>>(emptyList())
    override val teachers: StateFlow<List<Teacher>> = _teachers.asStateFlow()
    
    private val _invitations = MutableStateFlow<List<Invitation>>(emptyList())
    override val invitations: StateFlow<List<Invitation>> = _invitations.asStateFlow()
    
    override suspend fun getTeachersByAcademyID(academyId: String) {
        val teacherModels = firebaseAcademyTeachersRepository.getTeachersByAcademyID(academyId)
        _teachers.value = teacherModels.map { it.toTeacher() }
        
        // También obtenemos las invitaciones pendientes
        val invitationModels = firebaseAcademyTeachersRepository.getInvitationsByAcademyID(academyId)
        _invitations.value = invitationModels.map { it.toInvitation() }
    }
    
    override suspend fun inviteTeacherToAcademy(invitation: Invitation): Boolean {
        val invitationModel = invitation.toFirebaseModel()
        return firebaseAcademyTeachersRepository.inviteTeacherToAcademy(invitationModel)
    }
    
    override suspend fun deleteInvitation(invitationId: String): Boolean {
        val success = firebaseAcademyTeachersRepository.deleteInvitation(invitationId)
        
        // Si se eliminó con éxito, actualizamos la lista local
        if (success) {
            val updatedList = _invitations.value.filter { it.id != invitationId }
            _invitations.value = updatedList
        }
        
        return success
    }
    
    override suspend fun checkUserExists(email: String): Boolean {
        return firebaseAcademyTeachersRepository.checkUserExists(email)
    }
    
    override suspend fun getUserInvitations(email: String, academyId: String): List<Invitation> {
        val invitationsResponse = firebaseAcademyTeachersRepository.getUserInvitations(email, academyId)
        return invitationsResponse.map { it.toInvitation() }
    }
    
    override suspend fun canCreateInvitation(email: String, academyId: String): Pair<Boolean, String> {
        // Primero verificar si el usuario existe
        val userExists = checkUserExists(email)
        if (!userExists) {
            return Pair(false, "El usuario con correo $email no existe en el sistema")
        }
        
        // Verificar si ya hay una invitación pendiente para este usuario y academia
        val existingInvitations = getUserInvitations(email, academyId)
        val pendingInvitation = existingInvitations.find { invitation ->
            invitation.status == InvitationStatus.PENDING
        }
        
        return if (pendingInvitation != null) {
            Pair(false, "Ya existe una invitación pendiente para esta academia")
        } else {
            Pair(true, "")
        }
    }
} 