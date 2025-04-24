package com.daxen.mydancekmpsharedui.core.firebase.user.models

import kotlinx.serialization.Serializable

@Serializable
data class AcademyUserModel(
    val uid: String = "",
    val email: String = "",
    val name: String = "",
    val role: String = "",
)
