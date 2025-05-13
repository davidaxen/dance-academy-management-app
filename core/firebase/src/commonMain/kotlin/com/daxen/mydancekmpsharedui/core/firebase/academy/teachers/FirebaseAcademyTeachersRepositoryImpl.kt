package com.daxen.mydancekmpsharedui.core.firebase.academy.teachers

import com.daxen.mydancekmpsharedui.core.firebase.academy.teachers.model.TeacherModel
import com.daxen.mydancekmpsharedui.core.firebase.academy.teachers.model.InvitationModel
import dev.gitlive.firebase.firestore.FirebaseFirestore
import kotlinx.datetime.Clock

class FirebaseAcademyTeachersRepositoryImpl(
    private val firestore: FirebaseFirestore
) : FirebaseAcademyTeachersRepository {
    
    override suspend fun getTeachersByAcademyID(id: String): List<TeacherModel> {
        return try {
            val documents = firestore.collection("academies")
                .document(id)
                .collection("teachers")
                .get()
                .documents
            
            documents.map { doc ->
                val teacher = doc.data(TeacherModel.serializer())
                if (teacher.uid.isEmpty()) {
                    teacher.copy(uid = doc.id)
                } else {
                    teacher
                }
            }
        } catch (e: Exception) {
            println("Error obteniendo profesores: ${e.message}")
            emptyList()
        }
    }

    override suspend fun inviteTeacherToAcademy(invitation: InvitationModel): Boolean {
        return try {
            val docRef = firestore.collection("invitations").document
            val invitationWithIdAndTimestamp = invitation.copy(
                id = docRef.id,
                createdAt = Clock.System.now()
            )
            docRef.set(invitationWithIdAndTimestamp)
            
            true
        } catch (e: Exception) {
            println("Error invitando profesor: ${e.message}")
            false
        }
    }
    
    override suspend fun getInvitationsByAcademyID(academyId: String): List<InvitationModel> {
        return try {
            val documents = firestore.collection("invitations")
                .where {
                    all(
                        "academyId" equalTo academyId,
                        "role" equalTo "teacher"
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
            println("Error obteniendo invitaciones: ${e.message}")
            emptyList()
        }
    }
    
    override suspend fun deleteInvitation(invitationId: String): Boolean {
        return try {
            firestore.collection("invitations").document(invitationId).delete()
            true
        } catch (e: Exception) {
            println("Error eliminando invitación: ${e.message}")
            false
        }
    }
    
    override suspend fun checkUserExists(email: String): Boolean {
        return try {
            val users = firestore.collection("users")
                .where {
                    "email" equalTo email
                }
                .get()
                .documents
            
            users.isNotEmpty()
        } catch (e: Exception) {
            println("Error verificando si existe el usuario: ${e.message}")
            false
        }
    }
    
    override suspend fun getUserInvitations(email: String): List<InvitationModel> {
        return try {
            val documents = firestore.collection("invitations")
                .where {
                    all(
                        "userId" equalTo email,
                        "role" equalTo "teacher"
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
            println("Error obteniendo invitaciones del usuario: ${e.message}")
            emptyList()
        }
    }
} 