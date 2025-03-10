package com.daxen.mydancekmpsharedui.data.user.model.mapper

import com.daxen.mydancekmpsharedui.core.firebase.user.response.UserResponse
import com.daxen.mydancekmpsharedui.data.user.model.User
import com.daxen.mydancekmpsharedui.data.user.model.UserRole

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