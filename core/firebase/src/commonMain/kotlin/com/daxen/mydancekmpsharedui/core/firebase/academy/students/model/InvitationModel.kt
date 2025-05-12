package com.daxen.mydancekmpsharedui.core.firebase.academy.students.model

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class InvitationModel(
    val id: String = "",
    val userId: String = "",
    val academyId: String = "",
    val role: String = "",
    val academyName: String = "",
    val status: String = "PENDING", // puede ser "PENDING", "ACCEPTED", "REJECTED"
    val createdAt: Instant = Instant.DISTANT_PAST
) 