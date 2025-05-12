package com.daxen.mydancekmpsharedui.core.firebase.user

import com.daxen.mydancekmpsharedui.core.firebase.auth.FirebaseAuthService
import com.daxen.mydancekmpsharedui.core.firebase.user.models.UserModel
import dev.gitlive.firebase.firestore.FirebaseFirestore

class FirebaseUserServiceImpl(
    private val firestore: FirebaseFirestore,
    private val firebaseAuthService: FirebaseAuthService
): FirebaseUserService {

    override suspend fun getCurrentUserData(): UserModel {
        val userId = firebaseAuthService.getCurrentUserId()

        if (userId != null) {
            val document = firestore.collection("users")
                .document(userId)
                .get()

            return if (document.exists) {
                document.data(UserModel.serializer()).copy(uid = userId)
            } else {
                UserModel(uid = userId)
            }
        }

        return UserModel()
    }

    override suspend fun saveUserToDatabase(userModel: UserModel) {
        firestore.collection("users")
            .document(userModel.uid)
            .set(userModel)
    }

    override suspend fun getUserToCheck(): UserModel {
        val userId = firebaseAuthService.getCurrentUserId()

        if (userId != null) {
            val document = firestore.collection("users")
                .document(userId)
                .get()

            return if (document.exists) {
                document.data(UserModel.serializer())
            } else {
                UserModel()
            }
        }

        return UserModel()
    }
}