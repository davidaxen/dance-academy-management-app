package com.daxen.mydancekmpsharedui.core.firebase.user

import com.daxen.mydancekmpsharedui.core.firebase.user.response.UserResponse

interface FirebaseUserService {
    suspend fun updateCurrentUser(): UserResponse
}