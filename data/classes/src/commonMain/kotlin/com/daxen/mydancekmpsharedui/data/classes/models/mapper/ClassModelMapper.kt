package com.daxen.mydancekmpsharedui.data.classes.models.mapper

import com.daxen.mydancekmpsharedui.core.firebase.classes.response.TeacherInfo as ResponseTeacherInfo
import com.daxen.mydancekmpsharedui.core.firebase.classes.response.WeeklyClassesResponse
import com.daxen.mydancekmpsharedui.core.firebase.classes.response.SpecificClassesResponse
import com.daxen.mydancekmpsharedui.data.classes.models.ClassModel
import com.daxen.mydancekmpsharedui.data.classes.models.SpecificClassModel
import com.daxen.mydancekmpsharedui.data.classes.models.TeacherInfo
import com.daxen.mydancekmpsharedui.data.classes.models.WeeklyClassModel

fun SpecificClassesResponse.toSpecificClassModel(): SpecificClassModel {
    return SpecificClassModel(
        date = date,
        data = toClassModel(id, name, hour, status, teachers)
    )
}

fun WeeklyClassesResponse.toWeeklyClassModel(): WeeklyClassModel {
    return WeeklyClassModel(
        dayOfWeek = dayOfWeek,
        data = toClassModel(id, name, hour, status, teachers)
    )
}

private fun toClassModel(
    id: String, 
    name: String, 
    hour: String, 
    status: String, 
    teachers: List<ResponseTeacherInfo>
): ClassModel {
    // Obtener el primer profesor como principal (para compatibilidad)
    val teacherId = if (teachers.isNotEmpty()) teachers[0].id else ""
    
    return ClassModel(
        id = id,
        name = name,
        hour = hour,
        status = status,
        teacherId = teacherId,
        teachers = teachers.map { it.toTeacherInfo() }
    )
}

fun ResponseTeacherInfo.toTeacherInfo(): TeacherInfo {
    return TeacherInfo(
        id = id,
        name = name
    )
}