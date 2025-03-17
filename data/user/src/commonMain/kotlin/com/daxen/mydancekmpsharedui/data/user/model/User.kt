package com.daxen.mydancekmpsharedui.data.user.model

data class User(
    var uid: String = "",
    var email: String = "",
    var role: UserRole = UserRole.STUDENT,
    var name: String = "",
) {
    companion object {
        val EMPTY = User()
    }
}