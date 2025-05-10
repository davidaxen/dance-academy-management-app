package com.daxen.mydancekmpsharedui.core.firebase.academy.students

import com.daxen.mydancekmpsharedui.core.firebase.academy.students.model.StudentModel
import dev.gitlive.firebase.firestore.FirebaseFirestore

class FirebaseAcademyStudentsRepositoryImpl(
    private val firestore: FirebaseFirestore
): FirebaseAcademyStudentsRepository {
    override suspend fun getStudentsByAcademyID(id: String): List<StudentModel> {
        val documents = firestore.collection("academies")
            .document(id)
            .collection("students")
            .get()
            .documents

        return documents.map { doc ->
            doc.data(StudentModel.serializer()).let { data ->
                StudentModel(
                    uid = doc.id,
                    email = data.email,
                    name = data.name,
                    lastName = data.lastName,
                    birthDate = data.birthDate,
                    phoneNumber = data.phoneNumber,
                    phoneNumberPrefix = data.phoneNumberPrefix,
                    danceRole = data.danceRole
                )
            }
        }
    }

    override suspend fun inviteStudentToAcademy(): Boolean {
        return false
    }
}