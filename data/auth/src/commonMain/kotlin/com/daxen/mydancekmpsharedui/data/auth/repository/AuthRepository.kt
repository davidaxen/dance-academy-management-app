package com.daxen.mydancekmpsharedui.data.auth.repository

import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
    suspend fun login(email: String, password: String)
    suspend fun register(email: String, password: String)
    suspend fun logout()
    val currentUid: StateFlow<String?>
    val currentEmail: StateFlow<String?>
}