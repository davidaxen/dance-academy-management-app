package com.daxen.mydancekmpsharedui.core.firebase.user

import com.daxen.mydancekmpsharedui.core.firebase.auth.FirebaseAuthService
import com.daxen.mydancekmpsharedui.core.firebase.user.models.AcademyModel
import com.daxen.mydancekmpsharedui.core.firebase.user.models.AcademyUserModel
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.storage.storage

class FirebaseAcademyUserServiceImpl(
    private val firestore: FirebaseFirestore,
    private val firebaseAuthService: FirebaseAuthService
): FirebaseAcademyUserService {
    override suspend fun saveUserToDatabase(
        academyUserModel: AcademyUserModel,
        academyModel: AcademyModel,
        image: ByteArray
    ) {
        val academy = firestore.collection("academies")
            .add(academyModel)
        val academyId = academy.id

        // Subir la imagen y obtener URL
        val logoUrl = uploadImageToStorage(image, "academyLogos/$academyId")
        
        // Actualizar el modelo de academia con la URL del logo
        academyModel.logoUrl = logoUrl
        firestore.collection("academies")
            .document(academyId)
            .update(academyModel)

        firestore.collection("users")
            .document(academyUserModel.uid)
            .set(academyUserModel.copy(
                academyId = academyId
            ))
    }

    override suspend fun getCurrentAcademyUserData(): AcademyUserModel {
        val userId = firebaseAuthService.getCurrentUserId()
        if (userId != null) {
            val document = firestore.collection("users")
                .document(userId)
                .get()

            return if (document.exists) {
                document.data(AcademyUserModel.serializer()).let {
                    return it.copy(uid = userId)
                }
            } else {
                AcademyUserModel()
            }
        }
        return AcademyUserModel()
    }

    override suspend fun getCurrentAcademyData(academyId: String): AcademyModel {
        val userId = firebaseAuthService.getCurrentUserId()
        if (userId != null) {
            val document = firestore.collection("academies")
                .document(academyId)
                .get()

            return if (document.exists) {
                document.data(AcademyModel.serializer())
            } else {
                AcademyModel()
            }
        }
        return AcademyModel()
    }
    
    override suspend fun getAcademyLogoUrl(academyId: String): String {
        try {
            // Intentar obtener la URL desde Firestore primero
            val document = firestore.collection("academies")
                .document(academyId)
                .get()
                
            if (document.exists) {
                val academy = document.data(AcademyModel.serializer())
                if (academy.logoUrl.isNotEmpty()) {
                    return academy.logoUrl
                }
            }
            
            // Si no se encuentra en Firestore, intentar obtenerla directamente de Storage
            val storageRef = Firebase.storage.reference.child("academyLogos/$academyId")
            return storageRef.getDownloadUrl()
        } catch (e: Exception) {
            println("Error al obtener URL de logo: ${e.message}")
            return ""
        }
    }
}