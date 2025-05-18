package com.daxen.mydancekmpsharedui.data.teacher.classes.repository

import com.daxen.mydancekmpsharedui.data.academy.classes.models.SpecificClassModel
import com.daxen.mydancekmpsharedui.data.academy.classes.models.WeeklyClassModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.LocalDate

interface TeacherClassesRepository {
    suspend fun getWeeklyClassesByTeacherAndAcademyId(
        teacherId: String,
        academyId: String,
        date: LocalDate
    ): List<WeeklyClassModel>
    
    suspend fun getSpecificClassesByTeacherAndAcademyId(
        teacherId: String,
        academyId: String,
        date: LocalDate
    ): List<SpecificClassModel>
    
    val weeklyClassesList: StateFlow<List<WeeklyClassModel>>
    val specificClassesList: StateFlow<List<SpecificClassModel>>
} 