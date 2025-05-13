package com.daxen.mydancekmpsharedui.core.firebase.classes.response

import kotlinx.serialization.Serializable

@Serializable
data class TeacherInfo(
    val id: String = "",
    val name: String = ""
)

interface BaseClassResponse {
    val id: String
    val name: String
    val hour: String
    val status: String
    val teachers: List<TeacherInfo>
}