package com.daxen.mydancekmpsharedui.core.firebase.user

import com.daxen.mydancekmpsharedui.core.firebase.user.models.AcademyModel
import com.daxen.mydancekmpsharedui.core.firebase.user.models.AcademyUserModel

interface FirebaseAcademyUserService {
    suspend fun saveUserAndAcademy(academyUserModel: AcademyUserModel, academyModel: AcademyModel, image: ByteArray): String
    suspend fun getCurrentAcademyUserData(): AcademyUserModel
    suspend fun getCurrentAcademyData(academyId: String): AcademyModel
    suspend fun getAcademyLogoUrl(academyId: String): String
}