package com.daxen.mydancekmpsharedui.data.auth.model.mapper

import com.daxen.mydancekmpsharedui.core.firebase.auth.response.UserResponse
import com.daxen.mydancekmpsharedui.data.auth.model.User
import com.daxen.mydancekmpsharedui.data.auth.model.UserRole

fun UserResponse.toUser(): User {
    val role = UserRole.from(this.role)
        ?: throw IllegalArgumentException("Rol inválido o no especificado para el usuario con UID: ${this.uid}")

    return User(
        uid = this.uid,
        email = this.email,
        role = role,
        name = this.name,
    )
}