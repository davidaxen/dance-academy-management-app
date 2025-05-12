package com.daxen.mydancekmpsharedui.core.firebase.academy.teachers.model

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class InvitationModel(
    val id: String = "",
    val userId: String = "",
    val academyId: String = "",
    val role: String = "",
    val academyName: String = "",
    val status: String = "",
    val createdAt: Instant = Instant.fromEpochMilliseconds(0)
) 