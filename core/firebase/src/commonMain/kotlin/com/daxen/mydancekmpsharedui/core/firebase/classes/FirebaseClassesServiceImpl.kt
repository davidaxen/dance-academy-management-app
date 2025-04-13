package com.daxen.mydancekmpsharedui.core.firebase.classes

import com.daxen.mydancekmpsharedui.core.firebase.classes.response.SpecificClassesResponse
import com.daxen.mydancekmpsharedui.core.firebase.classes.response.WeeklyClassesResponse
import dev.gitlive.firebase.firestore.FirebaseFirestore
import kotlinx.serialization.DeserializationStrategy

class FirebaseClassesServiceImpl(
    private val firestore: FirebaseFirestore,
) : FirebaseClassesService {
    private suspend inline fun <reified T, R>getClassesByAcademyId(
        academyId: String,
        collectionName: String,
        deserializer: DeserializationStrategy<T>,
        map: (T, String) -> R
    ): List<R> {
        val documents = firestore
            .collection("/academies/$academyId/$collectionName")
            .get()
            .documents

        return documents.mapNotNull { doc ->
            doc.data(deserializer)?.let { data ->
                map(data, doc.id)
            }
        }
    }

    override suspend fun getWeeklyClassesByAcademyId(id: String): List<WeeklyClassesResponse> {
        return getClassesByAcademyId(
            academyId = id,
            collectionName = "classes",
            deserializer = WeeklyClassesResponse.serializer()
        ) { response, uid ->
            WeeklyClassesResponse(
                dayOfWeek = response.dayOfWeek,
                id = uid,
                name = response.name,
                hour = response.hour,
                status = response.status,
                teacherId = response.teacherId
            )
        }
    }

    override suspend fun getSpecificClassesByAcademyId(id: String): List<SpecificClassesResponse> {
        return getClassesByAcademyId(
            academyId = id,
            collectionName = "specificClasses",
            deserializer = SpecificClassesResponse.serializer()
        ) { response, uid ->
            SpecificClassesResponse(
                date = response.date,
                id = uid,
                name = response.name,
                hour = response.hour,
                status = response.status,
                teacherId = response.teacherId
            )
        }
    }


}