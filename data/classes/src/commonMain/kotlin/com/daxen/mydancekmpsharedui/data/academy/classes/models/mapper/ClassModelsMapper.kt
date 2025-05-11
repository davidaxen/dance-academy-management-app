package com.daxen.mydancekmpsharedui.data.academy.classes.models.mapper

import com.daxen.mydancekmpsharedui.core.firebase.academy.classes.model.SpecificClassModel as FirebaseSpecificClassModel
import com.daxen.mydancekmpsharedui.core.firebase.academy.classes.model.WeeklyClassModel as FirebaseWeeklyClassModel
import com.daxen.mydancekmpsharedui.core.firebase.academy.classes.model.TeacherInfo
import com.daxen.mydancekmpsharedui.data.academy.classes.models.SpecificClassModel
import com.daxen.mydancekmpsharedui.data.academy.classes.models.TeacherModel
import com.daxen.mydancekmpsharedui.data.academy.classes.models.WeeklyClassModel

fun TeacherInfo.toTeacherModel(): TeacherModel {
    return TeacherModel(
        id = id,
        name = name
    )
}

fun FirebaseWeeklyClassModel.toWeeklyClassModel(): WeeklyClassModel {
    return WeeklyClassModel(
        dayOfWeek = dayOfWeek,
        id = id,
        name = name,
        hour = hour,
        status = status,
        teachers = teachers.map { it.toTeacherModel() }
    )
}

fun FirebaseSpecificClassModel.toSpecificClassModel(): SpecificClassModel {
    return SpecificClassModel(
        date = date,
        id = id,
        name = name,
        hour = hour,
        status = status,
        teachers = teachers.map { it.toTeacherModel() }
    )
}

fun WeeklyClassModel.toFirebaseModel(): FirebaseWeeklyClassModel {
    return FirebaseWeeklyClassModel(
        dayOfWeek = dayOfWeek,
        id = id,
        name = name,
        hour = hour,
        status = status,
        teachers = teachers.map { TeacherInfo(it.id, it.name) }
    )
}

fun SpecificClassModel.toFirebaseModel(): FirebaseSpecificClassModel {
    return FirebaseSpecificClassModel(
        date = date,
        id = id,
        name = name,
        hour = hour,
        status = status,
        teachers = teachers.map { TeacherInfo(it.id, it.name) }
    )
} 