package com.daxen.mydancekmpsharedui.core.firebase.user

import com.daxen.mydancekmpsharedui.core.firebase.auth.FirebaseAuthService
import com.daxen.mydancekmpsharedui.core.firebase.user.response.UserResponse
import dev.gitlive.firebase.firestore.FirebaseFirestore

class FirebaseUserServiceImpl(
    private val firestore: FirebaseFirestore,
    private val firebaseAuthService: FirebaseAuthService
): FirebaseUserService {

    override suspend fun getCurrentUserData(): UserResponse {
        val userId = firebaseAuthService.getCurrentUserId()
            ?: throw IllegalStateException("ID de usuario nulo")

        val document = firestore.collection("users")
            .document(userId)
            .get()

        if (!document.exists) {
            throw IllegalStateException("No se encontraron datos del usuario")
        }

        return document.data(UserResponse.serializer()).copy(uid = userId)

    }

}