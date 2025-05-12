package com.daxen.mydancekmpsharedui.core.firebase.academy.teachers

import com.daxen.mydancekmpsharedui.core.firebase.academy.teachers.model.TeacherModel
import com.daxen.mydancekmpsharedui.core.firebase.academy.teachers.model.InvitationModel

interface FirebaseAcademyTeachersRepository {
    suspend fun getTeachersByAcademyID(id: String): List<TeacherModel>
    suspend fun inviteTeacherToAcademy(invitation: InvitationModel): Boolean
    suspend fun getInvitationsByAcademyID(academyId: String): List<InvitationModel>
    suspend fun deleteInvitation(invitationId: String): Boolean
    suspend fun checkUserExists(email: String): Boolean
    suspend fun getUserInvitations(email: String): List<InvitationModel>
} 