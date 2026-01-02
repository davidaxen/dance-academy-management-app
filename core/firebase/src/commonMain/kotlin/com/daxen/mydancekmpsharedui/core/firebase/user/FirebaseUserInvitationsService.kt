package com.daxen.mydancekmpsharedui.core.firebase.user

import com.daxen.mydancekmpsharedui.core.firebase.academy.students.model.InvitationModel
import com.daxen.mydancekmpsharedui.core.firebase.user.models.AcademyWithRoleModel

interface FirebaseUserInvitationsService {
    suspend fun getUserInvitations(email: String): List<InvitationModel>
    suspend fun acceptInvitation(invitation: InvitationModel): Boolean
    suspend fun rejectInvitation(invitationId: String): Boolean
    suspend fun getUserAcademies(email: String): List<String> // Devuelve los IDs de las academias
    suspend fun getAcademyDetails(academyId: String, userId: String): AcademyWithRoleModel // Devuelve los detalles de una academia con el rol del usuario
    suspend fun getAcademyLogoUrl(academyId: String): String // Devuelve la URL de la imagen de logo de la academia
} 