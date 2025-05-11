package com.daxen.mydancekmpsharedui.data.students.model

import kotlinx.datetime.Instant

data class Invitation(
    val id: String = "",
    val email: String,
    val academyId: String,
    val academyName: String,
    val status: InvitationStatus,
    val createdAt: Instant = Instant.DISTANT_PAST
)

enum class InvitationStatus(val value: String, val localizedValue: String) {
    PENDING("PENDING", "Pendiente"),
    ACCEPTED("ACCEPTED", "Aceptada"),
    REJECTED("REJECTED", "Rechazada"),;

    companion object {
        fun fromValue(value: String): InvitationStatus {
            return entries.firstOrNull { it.value == value } ?: PENDING
        }
    }
}