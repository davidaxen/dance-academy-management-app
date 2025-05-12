package com.daxen.mydancekmpsharedui.data.academy.classes.repository

import com.daxen.mydancekmpsharedui.data.academy.classes.models.SpecificClassModel
import com.daxen.mydancekmpsharedui.data.academy.classes.models.WeeklyClassModel
import kotlinx.coroutines.flow.StateFlow

interface AcademyClassesRepository {
    suspend fun getWeeklyClassesByAcademyId(academyId: String): List<WeeklyClassModel>
    suspend fun getSpecificClassesByAcademyId(academyId: String): List<SpecificClassModel>
    
    suspend fun saveWeeklyClass(academyId: String, weeklyClass: WeeklyClassModel): Result<WeeklyClassModel>
    suspend fun saveSpecificClass(academyId: String, specificClass: SpecificClassModel): Result<SpecificClassModel>
    
    suspend fun deleteWeeklyClass(academyId: String, classId: String): Result<Boolean>
    suspend fun deleteSpecificClass(academyId: String, classId: String): Result<Boolean>
    
    val weeklyClassesList: StateFlow<List<WeeklyClassModel>>
    val specificClassesList: StateFlow<List<SpecificClassModel>>
} 