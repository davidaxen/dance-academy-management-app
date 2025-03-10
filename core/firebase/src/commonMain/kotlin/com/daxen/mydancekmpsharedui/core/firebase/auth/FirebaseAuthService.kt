package com.daxen.mydancekmpsharedui.core.firebase.auth

import com.daxen.mydancekmpsharedui.core.firebase.auth.response.UserResponse

interface FirebaseAuthService {
    suspend fun login(email: String, password: String): UserResponse
    //    suspend fun register(email: String, password: String): Result<FirebaseUser?>
    suspend fun logout()
    suspend fun getCurrentUser()
}