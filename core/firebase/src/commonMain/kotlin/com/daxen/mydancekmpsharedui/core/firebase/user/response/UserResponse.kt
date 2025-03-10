package com.daxen.mydancekmpsharedui.core.firebase.user.response

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val uid: String = "",
    val email: String = "",
    val role: String = "" , // Rol como String desde Firebase
    val name: String = "",
)