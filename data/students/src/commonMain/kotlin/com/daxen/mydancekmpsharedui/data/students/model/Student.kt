package com.daxen.mydancekmpsharedui.data.students.model

data class Student(
    var uid: String = "",
    var email: String = "",
    var name: String = "",
    var lastName: String = "",
    var birthDate: String = "",
    var phoneNumber: String = "",
    var phoneNumberPrefix: String = "",
    var danceRole: DanceRole = DanceRole.LEADER,
    var profileImageUrl: String? = null,
)