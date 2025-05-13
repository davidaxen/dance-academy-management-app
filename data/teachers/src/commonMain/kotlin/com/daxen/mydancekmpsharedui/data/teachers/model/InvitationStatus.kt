package com.daxen.mydancekmpsharedui.data.teachers.model

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