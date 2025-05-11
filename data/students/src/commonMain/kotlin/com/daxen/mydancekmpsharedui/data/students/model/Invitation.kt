package com.daxen.mydancekmpsharedui.data.students.model

data class Invitation(
    val email: String,
    val academyId: String,
    val academyName: String,
    val status: InvitationStatus
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