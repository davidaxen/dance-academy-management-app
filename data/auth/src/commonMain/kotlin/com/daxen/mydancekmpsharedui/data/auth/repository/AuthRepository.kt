package com.daxen.mydancekmpsharedui.data.auth.repository

import com.daxen.mydancekmpsharedui.data.auth.model.User
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
    suspend fun login(email: String, password: String)
    suspend fun register(email: String, password: String)
    suspend fun logout()
//    val currentUser: StateFlow<User?>

}