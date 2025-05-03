package com.daxen.mydancekmpsharedui.data.user.model

data class User(
    var uid: String = "",
    var email: String = "",
    var name: String = "",
    var lastName: String = "",
    var birthDate: String = "",
    var phoneNumber: String = "",
    var phoneNumberPrefix: String = "",
    var role: UserRole = UserRole.STUDENT,
    var danceRole: DanceRole = DanceRole.LEADER,
    var academies: Map<String, UserAcademyInfo> = emptyMap()
) {
    companion object {
        val EMPTY = User()
    }
}