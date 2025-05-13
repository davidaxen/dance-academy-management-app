package com.daxen.mydancekmpsharedui.core.firebase.user.models

import kotlinx.serialization.Serializable

@Serializable
data class UserWithAcademyRolesModel(
    val uid: String = "",
    val email: String = "",
    val name: String = "",
    val academyRoles: Map<String, List<String>> = emptyMap()
) 