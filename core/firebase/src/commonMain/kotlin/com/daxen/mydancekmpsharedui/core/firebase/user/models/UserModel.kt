package com.daxen.mydancekmpsharedui.core.firebase.user.models

import kotlinx.serialization.Serializable

@Serializable
data class UserModel(
    val uid: String = "",
    val email: String = "",
    val name: String = "",
    val lastName: String = "",
    val birthDate: String = "",
    val phoneNumber: String = "",
    val phoneNumberPrefix: String = "",
    val role: String = "" , // Rol como String desde Firebase
    val danceRole: String = "" , // Rol como String desde Firebase
    val academies: Map<String, UserAcademyInfoResponse> = emptyMap()
)