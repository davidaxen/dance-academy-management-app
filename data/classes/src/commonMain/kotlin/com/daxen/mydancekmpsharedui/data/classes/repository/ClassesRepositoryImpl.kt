package com.daxen.mydancekmpsharedui.data.classes.repository

import com.daxen.mydancekmpsharedui.core.firebase.classes.FirebaseClassesService
import com.daxen.mydancekmpsharedui.data.classes.models.SpecificClassModel
import com.daxen.mydancekmpsharedui.data.classes.models.WeeklyClassModel
import com.daxen.mydancekmpsharedui.data.classes.models.mapper.toSpecificClassModel
import com.daxen.mydancekmpsharedui.data.classes.models.mapper.toWeeklyClassModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ClassesRepositoryImpl(
    private val firebaseClassesService: FirebaseClassesService
) : ClassesRepository {
    private val _weeklyClassesList = MutableStateFlow<List<WeeklyClassModel>>(emptyList())
    override val weeklyClassesList: StateFlow<List<WeeklyClassModel>> = _weeklyClassesList

    private val _specificClassesList = MutableStateFlow<List<SpecificClassModel>>(emptyList())
    override val specificClassesList: StateFlow<List<SpecificClassModel>> = _specificClassesList

    override suspend fun getClassesByAcademyId(id: String) {
        val weeklyResponses = firebaseClassesService.getWeeklyClassesByAcademyId(id)
        val specificResponses = firebaseClassesService.getSpecificClassesByAcademyId(id)

        _weeklyClassesList.value = weeklyResponses
            .takeIf { it.isNotEmpty() }
            ?.map { it.toWeeklyClassModel() }
            ?: emptyList()

        _specificClassesList.value = specificResponses
            .takeIf { it.isNotEmpty() }
            ?.map { it.toSpecificClassModel() }
            ?: emptyList()
    }
}