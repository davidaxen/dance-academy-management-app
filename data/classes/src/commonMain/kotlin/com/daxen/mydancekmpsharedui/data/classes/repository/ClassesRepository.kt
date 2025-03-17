package com.daxen.mydancekmpsharedui.data.classes.repository

import com.daxen.mydancekmpsharedui.data.classes.model.ClassModel
import kotlinx.coroutines.flow.StateFlow

interface ClassesRepository {
    suspend fun getClassesByAcademyId(id: String)
    val classesList: StateFlow<List<ClassModel>?>
}