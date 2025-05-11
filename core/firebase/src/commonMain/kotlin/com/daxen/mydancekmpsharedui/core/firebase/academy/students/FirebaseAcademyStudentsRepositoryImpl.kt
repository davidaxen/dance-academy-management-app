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
            // Generamos un nuevo documento y obtenemos su ID
            val docRef = firestore.collection("invitations").document
            
            // Actualizamos el modelo con el ID generado
            val invitationWithId = invitation.copy(id = docRef.id)
            
            // Guardamos el documento con su ID
            docRef.set(invitationWithId)
            
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
                // Aseguramos que el ID del documento esté incluido en el modelo
                val invitation = doc.data(InvitationModel.serializer())
                if (invitation.id.isEmpty()) {
                    invitation.copy(id = doc.id)
                } else {
                    invitation
                }
            }
        } catch (e: Exception) {
            println("Error getting invitations: ${e.message}")
            emptyList()
        }
    }
    
    override suspend fun deleteInvitation(invitationId: String): Boolean {
        return try {
            // Eliminamos directamente usando el ID
            firestore.collection("invitations").document(invitationId).delete()
            true
        } catch (e: Exception) {
            println("Error deleting invitation: ${e.message}")
            false
        }
    }
}