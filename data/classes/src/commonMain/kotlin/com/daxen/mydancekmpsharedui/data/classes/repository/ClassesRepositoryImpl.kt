package com.daxen.mydancekmpsharedui.data.classes.repository

import com.daxen.mydancekmpsharedui.core.firebase.classes.FirebaseClassesService
import com.daxen.mydancekmpsharedui.data.classes.model.ClassModel
import com.daxen.mydancekmpsharedui.data.classes.model.mapper.toClassModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ClassesRepositoryImpl(
    private val firebaseClassesService: FirebaseClassesService
) : ClassesRepository {
    private val _classesList = MutableStateFlow<List<ClassModel>?>(null)
    override val classesList: StateFlow<List<ClassModel>?> = _classesList


    override suspend fun getClassesByAcademyId(id: String) {
        val classesResponseList = firebaseClassesService.getClassesByAcademyId(id)
        if (classesResponseList.isEmpty()) {
            _classesList.value = emptyList()
            return
        }
        val classesModelList = mutableListOf<ClassModel>()
        for (classResponse in classesResponseList) {
            classesModelList.add(classResponse.toClassModel())
        }

        _classesList.value =  classesModelList



    }

}