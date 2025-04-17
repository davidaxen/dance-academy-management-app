package com.daxen.mydancekmpsharedui.core.firebase.auth


interface FirebaseAuthService {
    suspend fun login(email: String, password: String): String
    suspend fun register(email: String, password: String): String
    suspend fun logout()
    fun getCurrentUserId(): String?

}