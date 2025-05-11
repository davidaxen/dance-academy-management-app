package com.daxen.mydancekmpsharedui.data.students.model.mapper

import com.daxen.mydancekmpsharedui.core.firebase.academy.students.model.InvitationModel
import com.daxen.mydancekmpsharedui.data.students.model.Invitation
import com.daxen.mydancekmpsharedui.data.students.model.InvitationStatus

fun InvitationModel.toInvitation(): Invitation {
    return Invitation(
        email = this.userId, // Asumiendo que el userId es el email en este contexto
        academyId = this.academyId,
        academyName = this.academyName,
        status = InvitationStatus.fromValue(this.status)
    )
}

fun Invitation.toFirebaseModel(): InvitationModel {
    return InvitationModel(
        userId = this.email, // Guardamos el email como userId
        academyId = this.academyId,
        academyName = this.academyName,
        role = "student",
        status = this.status.value.lowercase() // Usamos el value del enum
    )
} 