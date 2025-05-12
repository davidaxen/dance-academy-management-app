package com.daxen.mydancekmpsharedui.data.students.model.mapper

import com.daxen.mydancekmpsharedui.core.firebase.academy.students.model.InvitationModel
import com.daxen.mydancekmpsharedui.data.students.model.Invitation
import com.daxen.mydancekmpsharedui.data.students.model.InvitationStatus

fun InvitationModel.toInvitation(): Invitation {
    return Invitation(
        id = this.id,
        email = this.userId, // Asumiendo que el userId es el email en este contexto
        academyId = this.academyId,
        academyName = this.academyName,
        status = InvitationStatus.fromValue(this.status),
        createdAt = this.createdAt
    )
}

fun Invitation.toFirebaseModel(): InvitationModel {
    return InvitationModel(
        id = this.id,
        userId = this.email, // Guardamos el email como userId
        academyId = this.academyId,
        academyName = this.academyName,
        role = "student",
        status = this.status.value,
        createdAt = this.createdAt
    )
} 