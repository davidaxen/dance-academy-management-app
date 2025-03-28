package com.daxen.mydancekmpsharedui.data.classes.repository

import com.daxen.mydancekmpsharedui.data.classes.models.SpecificClassModel
import com.daxen.mydancekmpsharedui.data.classes.models.WeeklyClassModel
import kotlinx.coroutines.flow.StateFlow

interface ClassesRepository {
    suspend fun getClassesByAcademyId(id: String)
    val weeklyClassesList: StateFlow<List<WeeklyClassModel>>
    val specificClassesList: StateFlow<List<SpecificClassModel>>
}