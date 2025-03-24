package com.daxen.mydancekmpsharedui.core.firebase.classes.response

import kotlinx.serialization.Serializable

@Serializable
data class ClassesResponse(
    val name: String,
    val hour: String,
    val status: String,
    val teacherId: String,
    val dayOfWeek: String,
)