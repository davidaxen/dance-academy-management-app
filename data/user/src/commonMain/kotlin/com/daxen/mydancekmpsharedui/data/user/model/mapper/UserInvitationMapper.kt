package com.daxen.mydancekmpsharedui.data.user.model.mapper

import com.daxen.mydancekmpsharedui.core.firebase.academy.students.model.InvitationModel
import com.daxen.mydancekmpsharedui.data.user.model.InvitationStatus
import com.daxen.mydancekmpsharedui.data.user.model.UserInvitation
import com.daxen.mydancekmpsharedui.data.user.model.UserRole

suspend fun InvitationModel.toUserInvitation(getLogoUrl: suspend (String) -> String): UserInvitation {
    // Obtenemos la URL del logo de la academia
    val logoUrl = getLogoUrl(academyId)
    return UserInvitation(
        id = id,
        academyId = academyId,
        academyName = academyName,
        imageUrl = logoUrl, // Usamos la URL del logo obtenida
        role = UserRole.fromString(role),
        status = InvitationStatus.fromString(status),
        createdAt = createdAt
    )
} 