package com.daxen.mydancekmpsharedui.core.firebase.classes

import com.daxen.mydancekmpsharedui.core.firebase.classes.response.ClassesResponse

interface FirebaseClassesService {
    suspend fun getClassesByAcademyId(id: String): MutableList<ClassesResponse>
}