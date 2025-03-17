package com.daxen.mydancekmpsharedui.data.classes.model.mapper

import com.daxen.mydancekmpsharedui.core.firebase.classes.response.ClassesResponse
import com.daxen.mydancekmpsharedui.data.classes.model.ClassModel

fun ClassesResponse.toClassModel(): ClassModel {
    return ClassModel(
        name = name,
//        instructorId = instructorId,
        academyId = academyId,
        date = date,
        hour = hour,
        maxCapacity = maxCapacity,
        availableSpots = availableSpots,
        status = status,
    )

}