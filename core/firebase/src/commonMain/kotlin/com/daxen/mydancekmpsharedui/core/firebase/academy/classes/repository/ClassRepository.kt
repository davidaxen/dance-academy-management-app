package com.daxen.mydancekmpsharedui.core.firebase.academy.classes.repository

import com.daxen.mydancekmpsharedui.core.firebase.academy.classes.model.SpecificClassModel
import com.daxen.mydancekmpsharedui.core.firebase.academy.classes.model.StudentReservation
import com.daxen.mydancekmpsharedui.core.firebase.academy.classes.model.WeeklyClassModel

interface ClassRepository {
    suspend fun getWeeklyClassesByAcademyId(academyId: String): List<WeeklyClassModel>
    suspend fun getSpecificClassesByAcademyId(academyId: String): List<SpecificClassModel>
    
    suspend fun saveWeeklyClass(academyId: String, weeklyClass: WeeklyClassModel): Result<WeeklyClassModel>
    suspend fun saveSpecificClass(academyId: String, specificClass: SpecificClassModel): Result<SpecificClassModel>
    
    suspend fun deleteWeeklyClass(academyId: String, classId: String): Result<Unit>
    suspend fun deleteSpecificClass(academyId: String, classId: String): Result<Unit>

    suspend fun getWeeklyClassById(academyId: String, classId: String): Result<WeeklyClassModel>
    suspend fun getSpecificClassById(academyId: String, classId: String): Result<SpecificClassModel>
    
    suspend fun getStudentReservationsByClassAndDate(
        academyId: String,
        classId: String,
        date: String
    ): Result<List<StudentReservation>>
} 