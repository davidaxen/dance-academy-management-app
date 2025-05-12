package com.daxen.mydancekmpsharedui.data.academy.classes.repository

import com.daxen.mydancekmpsharedui.core.firebase.academy.classes.repository.ClassRepository
import com.daxen.mydancekmpsharedui.data.academy.classes.models.SpecificClassModel
import com.daxen.mydancekmpsharedui.data.academy.classes.models.WeeklyClassModel
import com.daxen.mydancekmpsharedui.data.academy.classes.models.mapper.toFirebaseModel
import com.daxen.mydancekmpsharedui.data.academy.classes.models.mapper.toSpecificClassModel
import com.daxen.mydancekmpsharedui.data.academy.classes.models.mapper.toWeeklyClassModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AcademyClassesRepositoryImpl(
    private val classRepository: ClassRepository
) : AcademyClassesRepository {
    private val _weeklyClassesList = MutableStateFlow<List<WeeklyClassModel>>(emptyList())
    override val weeklyClassesList: StateFlow<List<WeeklyClassModel>> = _weeklyClassesList

    private val _specificClassesList = MutableStateFlow<List<SpecificClassModel>>(emptyList())
    override val specificClassesList: StateFlow<List<SpecificClassModel>> = _specificClassesList

    override suspend fun getWeeklyClassesByAcademyId(academyId: String): List<WeeklyClassModel> {
        val classes = classRepository.getWeeklyClassesByAcademyId(academyId)
            .map { it.toWeeklyClassModel() }
        
        _weeklyClassesList.value = classes
        return classes
    }

    override suspend fun getSpecificClassesByAcademyId(academyId: String): List<SpecificClassModel> {
        val classes = classRepository.getSpecificClassesByAcademyId(academyId)
            .map { it.toSpecificClassModel() }
        
        _specificClassesList.value = classes
        return classes
    }

    override suspend fun saveWeeklyClass(academyId: String, weeklyClass: WeeklyClassModel): Result<WeeklyClassModel> {
        return classRepository.saveWeeklyClass(academyId, weeklyClass.toFirebaseModel())
            .map { it.toWeeklyClassModel() }
    }

    override suspend fun saveSpecificClass(academyId: String, specificClass: SpecificClassModel): Result<SpecificClassModel> {
        return classRepository.saveSpecificClass(academyId, specificClass.toFirebaseModel())
            .map { it.toSpecificClassModel() }
    }
    
    override suspend fun deleteWeeklyClass(academyId: String, classId: String): Result<Boolean> {
        return classRepository.deleteWeeklyClass(academyId, classId).fold(
            onSuccess = {
                // Actualizamos la lista después de eliminar
                val updatedList = _weeklyClassesList.value.filter { it.id != classId }
                _weeklyClassesList.value = updatedList
                Result.success(true)
            },
            onFailure = {
                Result.failure(it)
            }
        )
    }
    
    override suspend fun deleteSpecificClass(academyId: String, classId: String): Result<Boolean> {
        return classRepository.deleteSpecificClass(academyId, classId).fold(
            onSuccess = {
                // Actualizamos la lista después de eliminar
                val updatedList = _specificClassesList.value.filter { it.id != classId }
                _specificClassesList.value = updatedList
                Result.success(true)
            },
            onFailure = {
                Result.failure(it)
            }
        )
    }
} 