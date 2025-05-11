package com.daxen.mydancekmpsharedui.core.firebase.academy.classes.repository

import com.daxen.mydancekmpsharedui.core.firebase.academy.classes.model.SpecificClassModel
import com.daxen.mydancekmpsharedui.core.firebase.academy.classes.model.WeeklyClassModel

interface ClassRepository {
    suspend fun getWeeklyClassesByAcademyId(academyId: String): List<WeeklyClassModel>
    suspend fun getSpecificClassesByAcademyId(academyId: String): List<SpecificClassModel>
    
    suspend fun saveWeeklyClass(academyId: String, weeklyClass: WeeklyClassModel): Result<WeeklyClassModel>
    suspend fun saveSpecificClass(academyId: String, specificClass: SpecificClassModel): Result<SpecificClassModel>
} 