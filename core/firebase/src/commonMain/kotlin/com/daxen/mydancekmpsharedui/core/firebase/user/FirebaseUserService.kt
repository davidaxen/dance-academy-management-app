package com.daxen.mydancekmpsharedui.core.firebase.user

import com.daxen.mydancekmpsharedui.core.firebase.user.models.UserModel

interface FirebaseUserService {
    suspend fun getCurrentUserData(): UserModel
    suspend fun saveUserToDatabase(userModel: UserModel)
}