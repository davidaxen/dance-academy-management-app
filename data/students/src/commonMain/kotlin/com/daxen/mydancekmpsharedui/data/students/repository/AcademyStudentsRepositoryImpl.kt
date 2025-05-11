package com.daxen.mydancekmpsharedui.data.students.repository

import com.daxen.mydancekmpsharedui.core.firebase.academy.students.FirebaseAcademyStudentsRepository
import com.daxen.mydancekmpsharedui.data.students.model.Invitation
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
    
    override suspend fun deleteInvitation(invitation: Invitation): Boolean {
        val success = firebaseAcademyStudentsRepository.deleteInvitation(
            email = invitation.email,
            academyId = invitation.academyId
        )
        
        // Si se eliminó con éxito, actualizamos la lista local
        if (success) {
            val updatedList = _invitations.value.filter { 
                it.email != invitation.email || it.academyId != invitation.academyId
            }
            _invitations.value = updatedList
        }
        
        return success
    }
}