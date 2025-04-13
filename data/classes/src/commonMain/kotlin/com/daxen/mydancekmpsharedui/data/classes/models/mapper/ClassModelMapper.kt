package com.daxen.mydancekmpsharedui.data.classes.models.mapper

import com.daxen.mydancekmpsharedui.core.firebase.classes.response.WeeklyClassesResponse
import com.daxen.mydancekmpsharedui.core.firebase.classes.response.SpecificClassesResponse
import com.daxen.mydancekmpsharedui.data.classes.models.ClassModel
import com.daxen.mydancekmpsharedui.data.classes.models.SpecificClassModel
import com.daxen.mydancekmpsharedui.data.classes.models.WeeklyClassModel

fun SpecificClassesResponse.toSpecificClassModel(): SpecificClassModel {
    return SpecificClassModel(
        date = date,
        data = toClassModel(id, name, hour, status, teacherId)
    )
}

fun WeeklyClassesResponse.toWeeklyClassModel(): WeeklyClassModel {
    return WeeklyClassModel(
        dayOfWeek = dayOfWeek,
        data = toClassModel(id, name, hour, status, teacherId)
    )
}

private fun toClassModel(id: String, name: String, hour: String, status: String, teacherId: String): ClassModel {
    return ClassModel(
        id = id,
        name = name,
        hour = hour,
        status = status,
        teacherId = teacherId,
    )
}