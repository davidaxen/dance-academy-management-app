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
//        val academyUserModel = AcademyUserModel(
//            uid = userModel.uid,
//            email = userModel.email,
//            name = userModel.name,
//            role = userModel.role
//        )
//        firestore.collection("academies")
//            .document(userModel.academies.keys.first())
//            .collection("students")
//            .document(academyUserModel.uid)
//            .set(academyUserModel)

        firestore.collection("users")
            .document(userModel.uid)
            .set(userModel)
    }

    override suspend fun isUserInfoComplete(): Boolean {
        val userId = firebaseAuthService.getCurrentUserId()

        if (userId != null) {
            return firestore.collection("users")
                .document(userId)
                .get()
                .exists
        }

        return false
    }
}