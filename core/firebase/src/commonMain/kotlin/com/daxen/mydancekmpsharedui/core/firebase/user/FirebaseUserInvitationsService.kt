package com.daxen.mydancekmpsharedui.core.firebase.user

import com.daxen.mydancekmpsharedui.core.firebase.academy.students.model.InvitationModel

interface FirebaseUserInvitationsService {
    suspend fun getUserInvitations(email: String): List<InvitationModel>
    suspend fun acceptInvitation(invitationId: String): Boolean
    suspend fun rejectInvitation(invitationId: String): Boolean
    suspend fun getUserAcademies(email: String): List<String> // Devuelve los IDs de las academias
    suspend fun getAcademyDetails(academyId: String, userId: String): Map<String, Any> // Devuelve los detalles de una academia
} 