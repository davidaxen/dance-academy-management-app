package com.daxen.mydancekmpsharedui.core.firebase.user

import com.daxen.mydancekmpsharedui.core.firebase.academy.students.model.InvitationModel
import com.daxen.mydancekmpsharedui.core.firebase.getCurrentTimestamp
import com.daxen.mydancekmpsharedui.core.firebase.user.models.AcademyDetailsModel
import com.daxen.mydancekmpsharedui.core.firebase.user.models.AcademyWithRoleModel
import com.daxen.mydancekmpsharedui.core.firebase.user.models.UserWithAcademyRolesModel
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.storage.storage

class FirebaseUserInvitationsServiceImpl(
    private val firestore: FirebaseFirestore
) : FirebaseUserInvitationsService {
    
    override suspend fun getUserInvitations(email: String): List<InvitationModel> {
        return try {
            val documents = firestore.collection("invitations")
                .where {
                    all(
                        "userId" equalTo email,
                        "status" equalTo "PENDING"
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
    
    override suspend fun acceptInvitation(invitation: InvitationModel): Boolean {
        return try {
            // Actualizamos el estado de la invitación a ACCEPTED
            firestore.collection("invitations")
                .document(invitation.id)
                .update("status" to "ACCEPTED")

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
                        .update("academyRoles.${invitation.academyId}" to listOf(invitation.role))

                    firestore.collection("academies")
                        .document(invitation.academyId)
                        .collection(if (invitation.role == "STUDENT") "students" else "teachers")
                        .document(userId)
                        .set(mapOf(
                            "userId" to userId,
                            "name" to userDoc.get("name"),
                            "lastName" to userDoc.get("lastName"),
                            "email" to invitation.userId, // This is the email
                            "joinedAt" to getCurrentTimestamp()
                        ))
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
                
                // Obtenemos el documento del usuario con sus roles en academias
                val userWithRoles = userDoc.data(UserWithAcademyRolesModel.serializer())
                
                // Devolvemos las claves (IDs de academias) del mapa de roles
                userWithRoles.academyRoles.keys.toList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            println("Error obteniendo academias del usuario: ${e.message}")
            emptyList()
        }
    }
    
    override suspend fun getAcademyDetails(academyId: String, userId: String): AcademyWithRoleModel {
        return try {
            // 1. Primero obtenemos los datos básicos de la academia
            val academyDoc = firestore.collection("academies")
                .document(academyId)
                .get()
                
            val academyDetails = academyDoc.data(AcademyDetailsModel.serializer())
            
            // 2. Obtenemos el rol del usuario en esta academia
            val userDoc = firestore.collection("users")
                .where {
                    "email" equalTo userId
                }
                .get()
                .documents
                .firstOrNull()
                
            var userRole = "STUDENT" // Por defecto, consideramos que es estudiante
            
            if (userDoc != null) {
                val userWithRoles = userDoc.data(UserWithAcademyRolesModel.serializer())
                val roles = userWithRoles.academyRoles[academyId]
                
                // Si tiene rol, usamos el primero (normalmente solo tendrá uno)
                if (!roles.isNullOrEmpty()) {
                    userRole = roles.first()
                }
            }
            
            // 3. Obtenemos la URL del logo de la academia
            val logoUrl = getAcademyLogoUrl(academyId)
            
            // Formateamos el horario como una cadena
            val schedule = "${academyDetails.openingTime} - ${academyDetails.closingTime}"
            
            // 4. Devolvemos un objeto AcademyWithRoleModel con los datos obtenidos
            AcademyWithRoleModel(
                id = academyId,
                name = academyDetails.name,
                location = academyDetails.address, // Usamos address en lugar de location
                imageUrl = logoUrl, // Usamos la URL del logo
                schedule = schedule, // Combinamos horarios de apertura y cierre
                role = userRole
            )
        } catch (e: Exception) {
            println("Error obteniendo detalles de la academia: $academyId - ${e.message}")
            // Devolvemos un modelo con valores por defecto
            throw e
        }
    }
    
    override suspend fun getAcademyLogoUrl(academyId: String): String {
        return try {
            // Obtenemos la referencia a la imagen en Storage
            val storageRef = Firebase.storage.reference
                .child("academyLogos")
                .child(academyId)
            
            // Obtenemos la URL de descarga
            storageRef.getDownloadUrl()
        } catch (e: Exception) {
            println("Error obteniendo URL del logo de la academia: $academyId - ${e.message}")
            "" // Devolvemos una cadena vacía en caso de error
        }
    }
} 