package com.daxen.mydancekmpsharedui.core.firebase.user

import com.daxen.mydancekmpsharedui.core.firebase.user.models.AcademyModel
import com.daxen.mydancekmpsharedui.core.firebase.user.models.AcademyUserModel

interface FirebaseAcademyUserService {
    suspend fun saveUserToDatabase(academyUserModel: AcademyUserModel, academyModel: AcademyModel, image: ByteArray)
    suspend fun getCurrentAcademyUserData(): AcademyUserModel
    suspend fun getCurrentAcademyData(academyId: String): AcademyModel
}