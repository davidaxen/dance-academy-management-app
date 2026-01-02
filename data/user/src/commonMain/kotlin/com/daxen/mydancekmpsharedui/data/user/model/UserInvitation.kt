package com.daxen.mydancekmpsharedui.data.user.model

import kotlinx.datetime.Instant

data class UserInvitation(
    val id: String,
    val academyId: String,
    val userId: String,
    val academyName: String,
    val imageUrl: String = "",
    val role: UserRole,
    val status: InvitationStatus = InvitationStatus.PENDING,
    val createdAt: Instant = Instant.DISTANT_PAST
)

enum class InvitationStatus {
    PENDING,
    ACCEPTED,
    REJECTED;
    
    companion object {
        fun fromString(status: String): InvitationStatus {
            return when (status.uppercase()) {
                "ACCEPTED" -> ACCEPTED
                "REJECTED" -> REJECTED
                else -> PENDING
            }
        }
    }
} 