package com.daxen.mydancekmpsharedui.data.teachers.repository

import com.daxen.mydancekmpsharedui.data.teachers.model.Teacher
import com.daxen.mydancekmpsharedui.data.teachers.model.Invitation
import kotlinx.coroutines.flow.StateFlow

interface AcademyTeachersRepository {
    val teachers: StateFlow<List<Teacher>>
    val invitations: StateFlow<List<Invitation>>
    
    suspend fun getTeachersByAcademyID(academyId: String)
    suspend fun inviteTeacherToAcademy(invitation: Invitation): Boolean
    suspend fun deleteInvitation(invitationId: String): Boolean
    suspend fun checkUserExists(email: String): Boolean
    suspend fun getUserInvitations(email: String): List<Invitation>
    suspend fun canCreateInvitation(email: String): Pair<Boolean, String>
} 