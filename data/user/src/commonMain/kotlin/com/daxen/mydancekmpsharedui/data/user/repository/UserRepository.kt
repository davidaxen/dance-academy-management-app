package com.daxen.mydancekmpsharedui.data.user.repository

import com.daxen.mydancekmpsharedui.data.user.model.DanceRole
import com.daxen.mydancekmpsharedui.data.user.model.User
import com.daxen.mydancekmpsharedui.data.user.model.UserRole
import kotlinx.coroutines.flow.StateFlow

interface UserRepository {
    val currentUser: StateFlow<User>
    suspend fun updateCurrentUser()
    suspend fun isUserInfoComplete(): Boolean
    fun setPersonalInfo(
        firstName: String,
        lastName: String,
        birthDate: String,
        phone: String,
        prefix: String
    )
    fun setDanceRole(role: DanceRole)
    fun setRole(role: UserRole)
    suspend fun saveUserToDatabase()
}