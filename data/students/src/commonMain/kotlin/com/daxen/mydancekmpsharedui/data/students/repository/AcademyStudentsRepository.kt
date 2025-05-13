package com.daxen.mydancekmpsharedui.data.students.repository

import com.daxen.mydancekmpsharedui.data.students.model.Invitation
import com.daxen.mydancekmpsharedui.data.students.model.Student
import kotlinx.coroutines.flow.StateFlow

interface AcademyStudentsRepository {
    val students: StateFlow<List<Student>>
    val invitations: StateFlow<List<Invitation>>
    
    suspend fun getStudentsByAcademyID(id: String)
    suspend fun getInvitationsByAcademyID(id: String)
    suspend fun inviteStudentToAcademy(invitation: Invitation): Boolean
    suspend fun deleteInvitation(invitationId: String): Boolean
    suspend fun checkUserExists(email: String): Boolean
    suspend fun getUserInvitations(email: String, academyId: String): List<Invitation>
    suspend fun canCreateInvitation(email: String, academyId: String): Pair<Boolean, String>
}