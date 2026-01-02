package com.daxen.mydancekmpsharedui.core.firebase.user.models

import kotlinx.serialization.Serializable

@Serializable
data class AcademyWithRoleModel(
    val id: String = "",
    val name: String = "",
    val location: String = "",
    val imageUrl: String = "",
    val schedule: String = "",
    val role: String = "STUDENT"
) 