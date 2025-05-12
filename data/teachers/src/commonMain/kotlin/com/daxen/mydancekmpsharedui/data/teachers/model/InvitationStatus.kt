package com.daxen.mydancekmpsharedui.data.teachers.model

enum class InvitationStatus(val value: String) {
    PENDING("pending"),
    ACCEPTED("accepted"),
    REJECTED("rejected");

    companion object {
        fun fromValue(value: String?): InvitationStatus {
            return entries.find { it.value == value } ?: PENDING
        }
    }
} 