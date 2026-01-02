package com.daxen.mydancekmpsharedui.core.firebase.academy.students

import com.daxen.mydancekmpsharedui.core.firebase.academy.students.model.StudentModel
import com.daxen.mydancekmpsharedui.core.firebase.academy.students.model.InvitationModel
import dev.gitlive.firebase.firestore.FirebaseFirestore
import kotlinx.datetime.Clock

class FirebaseAcademyStudentsRepositoryImpl(
    private val firestore: FirebaseFirestore
): FirebaseAcademyStudentsRepository {
    override suspend fun getStudentsByAcademyID(id: String): List<StudentModel> {
        val documents = firestore.collection("academies")
            .document(id)
            .collection("students")
            .get()
            .documents

        val firstListOfIds = documents.map { doc ->
            doc.id
        }

        return firstListOfIds.map {
            firestore.collection("users")
                .document(it)
                .get()
                .data(StudentModel.serializer())
        }
    }

    override suspend fun inviteStudentToAcademy(invitation: InvitationModel): Boolean {
        return try {
            val docRef = firestore.collection("invitations").document
            val invitationWithIdAndTimestamp = invitation.copy(
                id = docRef.id,
                createdAt = Clock.System.now()
            )
            docRef.set(invitationWithIdAndTimestamp)
            
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
    
    override suspend fun checkUserExists(email: String): Boolean {
        return try {
            // Buscamos usuarios donde el campo "email" coincida con el email proporcionado
            val querySnapshot = firestore.collection("users")
                .where {
                    "email" equalTo email
                }
                .get()
            
            querySnapshot.documents.isNotEmpty()
        } catch (e: Exception) {
            println("Error checking user existence: ${e.message}")
            false
        }
    }
    
    override suspend fun getUserInvitations(email: String, academyId: String): List<InvitationModel> {
        return try {
            // Buscamos invitaciones para el usuario especificado
            val documents = firestore.collection("invitations")
                .where {
                    all(
                        "userId" equalTo email,
                        "academyId" equalTo academyId,
                        "role" equalTo "STUDENT"
                    )
                }
                .get()
                .documents
            
            documents.map { doc ->
                val invitation = doc.data(InvitationModel.serializer())
                if (invitation.id.isEmpty()) {
                    invitation.copy(id = doc.id)
                } else {
                    invitation
                }
            }
        } catch (e: Exception) {
            println("Error getting user invitations: ${e.message}")
            emptyList()
        }
    }
}