package com.daxen.mydancekmpsharedui.core.firebase.academy.students

import com.daxen.mydancekmpsharedui.core.firebase.academy.students.model.StudentModel
import com.daxen.mydancekmpsharedui.core.firebase.academy.students.model.InvitationModel
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

    override suspend fun inviteStudentToAcademy(userId: String, academyId: String, academyName: String): Boolean {
        return try {
            val invitation = InvitationModel(
                userId = userId,
                academyId = academyId,
                role = "student",
                academyName = academyName
            )

            firestore.collection("invitations")
                .add(invitation)
            
            true
        } catch (e: Exception) {
            println("Error inviting student: ${e.message}")
            false
        }
    }
}