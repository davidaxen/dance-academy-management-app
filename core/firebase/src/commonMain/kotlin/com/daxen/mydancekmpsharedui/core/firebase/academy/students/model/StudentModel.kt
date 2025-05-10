package com.daxen.mydancekmpsharedui.core.firebase.academy.students.model

import kotlinx.serialization.Serializable

@Serializable
data class StudentModel(
    var uid: String = "",
    var email: String = "",
    var name: String = "",
    var lastName: String = "",
    var birthDate: String = "",
    var phoneNumber: String = "",
    var phoneNumberPrefix: String = "",
    var danceRole: String = "",
)