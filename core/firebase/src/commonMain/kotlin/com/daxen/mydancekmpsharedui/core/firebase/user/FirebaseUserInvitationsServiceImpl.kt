package com.daxen.mydancekmpsharedui.core.firebase.user

import com.daxen.mydancekmpsharedui.core.firebase.academy.students.model.InvitationModel
import dev.gitlive.firebase.firestore.FirebaseFirestore

class FirebaseUserInvitationsServiceImpl(
    private val firestore: FirebaseFirestore
) : FirebaseUserInvitationsService {
    
    override suspend fun getUserInvitations(email: String): List<InvitationModel> {
        return try {
            val documents = firestore.collection("invitations")
                .where {
                    "userId" equalTo email
                    "status" equalTo "PENDING"
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
    
    override suspend fun acceptInvitation(invitationId: String): Boolean {
        return try {
            // Actualizamos el estado de la invitación a ACCEPTED
            firestore.collection("invitations")
                .document(invitationId)
                .update("status" to "ACCEPTED")
            
            // Obtenemos los datos de la invitación
            val invitation = firestore.collection("invitations")
                .document(invitationId)
                .get()
                .data(InvitationModel.serializer())
            
            // Creamos la relación usuario-academia
            firestore.collection("users")
                .where {
                    "email" equalTo invitation.userId
                }
                .get()
                .documents
                .firstOrNull()?.let { userDoc ->
                    val userId = userDoc.id
                    
                    // Agregamos el rol del usuario en esta academia
                    firestore.collection("users")
                        .document(userId)
                        .update("academyRoles.${invitation.academyId}" to listOf(invitation.role.lowercase()))
                    
                    // Añadimos al usuario como estudiante en la academia
                    if (invitation.role.equals("STUDENT", ignoreCase = true)) {
                        firestore.collection("academies")
                            .document(invitation.academyId)
                            .collection("students")
                            .document(userId)
                            .set(mapOf(
                                "userId" to userId,
                                "name" to "",
                                "joinedAt" to com.daxen.mydancekmpsharedui.core.firebase.getCurrentTimestamp()
                            ))
                    }
                }
            
            true
        } catch (e: Exception) {
            println("Error aceptando invitación: ${e.message}")
            false
        }
    }
    
    override suspend fun rejectInvitation(invitationId: String): Boolean {
        return try {
            // Actualizamos el estado de la invitación a REJECTED
            firestore.collection("invitations")
                .document(invitationId)
                .update("status" to "REJECTED")
            
            true
        } catch (e: Exception) {
            println("Error rechazando invitación: ${e.message}")
            false
        }
    }
    
    override suspend fun getUserAcademies(email: String): List<String> {
        return try {
            // Buscamos el documento del usuario
            val userDocuments = firestore.collection("users")
                .where {
                    "email" equalTo email
                }
                .get()
                .documents
                
            if (userDocuments.isNotEmpty()) {
                val userId = userDocuments.first().id
                val userDoc = firestore.collection("users").document(userId).get()
                
                // Obtenemos el mapa de academyRoles
                val data = userDoc.data() as? Map<String, Any>
                val academyRoles = data?.get("academyRoles") as? Map<String, Any>
                
                academyRoles?.keys?.toList() ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            println("Error obteniendo academias del usuario: ${e.message}")
            emptyList()
        }
    }
    
    override suspend fun getAcademyDetails(academyId: String, userId: String): Map<String, Any> {
        return try {
            // 1. Primero obtenemos los datos básicos de la academia
            val academyDoc = firestore.collection("academies")
                .document(academyId)
                .get()
                
            val academyData = academyDoc.data() ?: emptyMap<String, Any>()
            
            // 2. Obtenemos el rol del usuario en esta academia
            val userDoc = firestore.collection("users")
                .where {
                    "email" equalTo userId
                }
                .get()
                .documents
                .firstOrNull()
                
            var userRole = "student" // Por defecto, consideramos que es estudiante
            
            if (userDoc != null) {
                val userData = userDoc.data() as? Map<String, Any>
                val academyRoles = userData?.get("academyRoles") as? Map<String, Any>
                val roles = academyRoles?.get(academyId) as? List<String>
                
                // Si tiene rol, usamos el primero (normalmente solo tendrá uno)
                if (roles != null && roles.isNotEmpty()) {
                    userRole = roles.first()
                }
            }
            
            // 3. Construimos el mapa de respuesta
            mapOf(
                "id" to academyId,
                "name" to (academyData["name"] as? String ?: "Academia sin nombre"),
                "location" to (academyData["location"] as? String ?: ""),
                "imageUrl" to (academyData["imageUrl"] as? String ?: ""),
                "schedule" to (academyData["schedule"] as? String ?: ""),
                "role" to userRole
            )
        } catch (e: Exception) {
            println("Error obteniendo detalles de la academia: $academyId - ${e.message}")
            // Devolvemos un mapa con valores por defecto
            mapOf(
                "id" to academyId,
                "name" to "Academia $academyId",
                "location" to "",
                "imageUrl" to "",
                "schedule" to "",
                "role" to "student"
            )
        }
    }
} 