package com.daxen.mydancekmpsharedui.core.firebase.academy.students.model

import kotlinx.serialization.Serializable

@Serializable
data class InvitationModel(
    val userId: String = "",
    val academyId: String = "",
    val role: String = "",
    val academyName: String = "",
    val status: String = "pending" // puede ser "pending", "accepted", "rejected"
) 