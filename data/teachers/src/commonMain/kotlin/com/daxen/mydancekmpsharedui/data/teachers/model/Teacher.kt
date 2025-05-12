package com.daxen.mydancekmpsharedui.data.teachers.model

data class Teacher(
    val uid: String,
    val name: String,
    val lastName: String,
    val email: String,
    val birthDate: String,
    val phoneNumber: String,
    val phoneNumberPrefix: String,
    val profileImageUrl: String? = null,
    val joinedAt: String = ""
) 