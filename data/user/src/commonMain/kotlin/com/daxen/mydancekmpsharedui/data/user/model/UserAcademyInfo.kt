package com.daxen.mydancekmpsharedui.data.user.model

data class UserAcademyInfo(
    val academyId: String,
    val role: UserRole = UserRole.STUDENT,
)