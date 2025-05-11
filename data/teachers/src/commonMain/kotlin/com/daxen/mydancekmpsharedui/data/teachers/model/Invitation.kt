package com.daxen.mydancekmpsharedui.data.teachers.model

import kotlinx.datetime.Instant

data class Invitation(
    val id: String,
    val email: String,
    val academyId: String,
    val academyName: String,
    val status: InvitationStatus,
    val createdAt: Instant
) 