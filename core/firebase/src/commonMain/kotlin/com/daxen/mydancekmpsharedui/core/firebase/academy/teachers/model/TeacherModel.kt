package com.daxen.mydancekmpsharedui.core.firebase.academy.teachers.model

import kotlinx.serialization.Serializable

@Serializable
data class TeacherModel(
    val uid: String = "",
    val name: String = "",
    val lastName: String = "",
    val email: String = "",
    val birthDate: String = "",
    val phoneNumber: String = "",
    val phoneNumberPrefix: String = "",
    val profileImageUrl: String? = null,
    val joinedAt: String = ""
) 