package com.daxen.mydancekmpsharedui.core.firebase.user

import com.daxen.mydancekmpsharedui.core.firebase.user.models.AcademyModel
import com.daxen.mydancekmpsharedui.core.firebase.user.models.AcademyUserModel
import dev.gitlive.firebase.firestore.FirebaseFirestore

class FirebaseAcademyUserServiceImpl(
    private val firestore: FirebaseFirestore,
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
}