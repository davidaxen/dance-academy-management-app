package com.daxen.mydancekmpsharedui.data.user.model.mapper

import com.daxen.mydancekmpsharedui.core.firebase.user.response.UserResponse
import com.daxen.mydancekmpsharedui.data.user.model.DanceRole
import com.daxen.mydancekmpsharedui.data.user.model.User
import com.daxen.mydancekmpsharedui.data.user.model.UserRole

fun UserResponse.toUser(): User {
    println(this)
    val role = UserRole.from(this.role)
//        ?: throw IllegalArgumentException("Rol inválido o no especificado para el usuario con UID: ${this.uid}")
        ?: UserRole.STUDENT

    val danceRole = DanceRole.from(this.danceRole)
//        ?: throw IllegalArgumentException("Rol de baile inválido o no especificado para el usuario con UID: ${this.uid}")
        ?: DanceRole.LEADER

    return User(
        uid = this.uid,
        email = this.email,
        name = this.name,
        role = role,
        danceRole = danceRole,
    )
}