package com.daxen.mydancekmpsharedui.data.user.model.mapper

import com.daxen.mydancekmpsharedui.core.firebase.academy.students.model.InvitationModel
import com.daxen.mydancekmpsharedui.data.user.model.InvitationStatus
import com.daxen.mydancekmpsharedui.data.user.model.UserInvitation
import com.daxen.mydancekmpsharedui.data.user.model.UserRole

fun InvitationModel.toUserInvitation(): UserInvitation {
    return UserInvitation(
        id = id,
        academyId = academyId,
        academyName = academyName,
        imageUrl = "", // La imagen se tendrá que obtener de otra colección
        role = UserRole.fromString(role),
        status = InvitationStatus.fromString(status),
        createdAt = createdAt
    )
} 