package com.daxen.mydancekmpsharedui.data.user.repository

import com.daxen.mydancekmpsharedui.data.user.model.User
import kotlinx.coroutines.flow.StateFlow

interface UserRepository {
    val currentUser: StateFlow<User>
    suspend fun updateCurrentUser()
    fun setDanceRole(role: String)
}