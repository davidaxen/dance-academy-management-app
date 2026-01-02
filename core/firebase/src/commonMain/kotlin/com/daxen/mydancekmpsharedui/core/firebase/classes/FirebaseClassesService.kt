package com.daxen.mydancekmpsharedui.core.firebase.classes

import com.daxen.mydancekmpsharedui.core.firebase.classes.response.SpecificClassesResponse
import com.daxen.mydancekmpsharedui.core.firebase.classes.response.WeeklyClassesResponse

interface FirebaseClassesService {
    suspend fun getWeeklyClassesByAcademyId(id: String): List<WeeklyClassesResponse>
    suspend fun getSpecificClassesByAcademyId(id: String): List<SpecificClassesResponse>
}