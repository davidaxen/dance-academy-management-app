package com.daxen.mydancekmpsharedui.core.firebase.academy.classes.model

import kotlinx.serialization.Serializable

@Serializable
data class WeeklyClassModel(
    val dayOfWeek: String,
    override val id: String = "",
    override val name: String = "",
    override val hour: String = "",
    override val status: String = "",
    override val teachers: List<TeacherInfo> = emptyList()
) : ClassModel 