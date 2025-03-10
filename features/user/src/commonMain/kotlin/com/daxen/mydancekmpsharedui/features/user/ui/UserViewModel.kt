package com.daxen.mydancekmpsharedui.features.user.ui

import androidx.lifecycle.ViewModel
import com.daxen.mydancekmpsharedui.data.user.model.User
import com.daxen.mydancekmpsharedui.data.user.repository.UserRepository
import kotlinx.coroutines.flow.StateFlow

class UserViewModel(
    private val userRepository: UserRepository,
): ViewModel() {
    val currentUser: StateFlow<User?> = userRepository.currentUser

}