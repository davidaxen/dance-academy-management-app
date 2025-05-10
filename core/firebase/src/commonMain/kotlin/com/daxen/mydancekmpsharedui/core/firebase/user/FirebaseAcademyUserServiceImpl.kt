package com.daxen.mydancekmpsharedui.core.firebase.user

import com.daxen.mydancekmpsharedui.core.firebase.auth.FirebaseAuthService
import com.daxen.mydancekmpsharedui.core.firebase.user.models.AcademyModel
import com.daxen.mydancekmpsharedui.core.firebase.user.models.AcademyUserModel
import dev.gitlive.firebase.firestore.FirebaseFirestore

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

        firestore.collection("users")
            .document(academyUserModel.uid)
            .set(academyUserModel.copy(
                academyId = academyId
            ))

        uploadImageToStorage(image,"academyLogos/$academyId")
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
}