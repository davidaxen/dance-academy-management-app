package com.daxen.mydancekmpsharedui.core.firebase.classes

import com.daxen.mydancekmpsharedui.core.firebase.classes.response.ClassesResponse
import dev.gitlive.firebase.firestore.FirebaseFirestore

class FirebaseClassesServiceImpl(
    private val firestore: FirebaseFirestore,
) : FirebaseClassesService {
    override suspend fun getClassesByAcademyId(id: String): MutableList<ClassesResponse> {
        val documents = firestore.collection("classes")
            .where {
                "academyId" equalTo id
            }.get().documents

        val classesResponseList = mutableListOf<ClassesResponse>()
        for (document in documents) {
            classesResponseList.add(document.data(ClassesResponse.serializer()))
        }
        return classesResponseList

    }


}