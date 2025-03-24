package com.daxen.mydancekmpsharedui.data.classes.model.mapper

import com.daxen.mydancekmpsharedui.core.firebase.classes.response.ClassesResponse
import com.daxen.mydancekmpsharedui.data.classes.model.ClassModel

fun ClassesResponse.toClassModel(): ClassModel {
    return ClassModel(
        name = name,
        hour = hour,
        status = status,
        teacherId = teacherId,
        day = dayOfWeek,
    )

}