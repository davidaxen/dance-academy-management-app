package com.daxen.mydancekmpsharedui.core.firebase.academy.students

import com.daxen.mydancekmpsharedui.core.firebase.academy.students.model.StudentModel
import com.daxen.mydancekmpsharedui.core.firebase.academy.students.model.InvitationModel

interface FirebaseAcademyStudentsRepository {
    suspend fun getStudentsByAcademyID(id: String): List<StudentModel>
    suspend fun inviteStudentToAcademy(invitation: InvitationModel): Boolean
    suspend fun getInvitationsByAcademyID(academyId: String): List<InvitationModel>
    suspend fun deleteInvitation(invitationId: String): Boolean
    suspend fun checkUserExists(email: String): Boolean
    suspend fun getUserInvitations(email: String, academyId: String): List<InvitationModel>
}