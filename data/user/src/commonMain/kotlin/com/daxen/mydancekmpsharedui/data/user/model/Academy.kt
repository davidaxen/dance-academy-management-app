package com.daxen.mydancekmpsharedui.data.user.model

data class Academy(
    val id: String,
    val name: String,
    val location: String,
    val imageUrl: String = "",
    val schedule: String = "",
    val role: UserRole
) 