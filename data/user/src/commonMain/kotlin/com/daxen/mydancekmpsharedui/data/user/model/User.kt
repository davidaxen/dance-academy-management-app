package com.daxen.mydancekmpsharedui.data.user.model

data class User(
    var uid: String = "",
    var email: String = "",
    var name: String = "",
    var role: UserRole = UserRole.STUDENT,
    var danceRole: DanceRole = DanceRole.LEADER,
) {
    companion object {
        val EMPTY = User()
    }
}