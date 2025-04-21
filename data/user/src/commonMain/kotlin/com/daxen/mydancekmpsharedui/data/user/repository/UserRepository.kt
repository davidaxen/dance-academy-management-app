package com.daxen.mydancekmpsharedui.data.user.repository

import com.daxen.mydancekmpsharedui.data.user.model.User
import kotlinx.coroutines.flow.StateFlow

interface UserRepository {
    val currentUser: StateFlow<User>
    suspend fun updateCurrentUser()
    fun setPersonalInfo(
        firstName: String,
        lastName: String,
        birthDate: String,
        phone: String,
        prefix: String
    )
    fun setDanceRole(role: String)
}