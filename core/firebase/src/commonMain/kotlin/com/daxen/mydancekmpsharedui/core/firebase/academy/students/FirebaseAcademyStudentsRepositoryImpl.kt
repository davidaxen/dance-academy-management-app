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

    override suspend fun inviteStudentToAcademy(invitation: InvitationModel): Boolean {
        return try {
            firestore.collection("invitations")
                .add(invitation)
            
            true
        } catch (e: Exception) {
            println("Error inviting student: ${e.message}")
            false
        }
    }
    
    override suspend fun getInvitationsByAcademyID(academyId: String): List<InvitationModel> {
        return try {
            val documents = firestore.collection("invitations")
                .where {
                    "academyId" equalTo academyId
                }
                .get()
                .documents
                
            documents.map { doc ->
                doc.data(InvitationModel.serializer())
            }
        } catch (e: Exception) {
            println("Error getting invitations: ${e.message}")
            emptyList()
        }
    }
    
    override suspend fun deleteInvitation(email: String, academyId: String): Boolean {
        return try {
            // Primero buscamos la invitación que coincida con el email (userId) y academyId
            val querySnapshot = firestore.collection("invitations")
                .where {
                    "userId" equalTo email
                    "academyId" equalTo academyId
                }
                .get()
            
            if (querySnapshot.documents.isEmpty()) {
                return false
            }
            
            // Eliminamos el primer documento que coincide
            val docId = querySnapshot.documents.first().id
            firestore.collection("invitations").document(docId).delete()
            true
        } catch (e: Exception) {
            println("Error deleting invitation: ${e.message}")
            false
        }
    }
}