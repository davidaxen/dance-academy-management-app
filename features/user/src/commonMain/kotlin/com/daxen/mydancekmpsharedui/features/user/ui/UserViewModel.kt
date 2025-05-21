package com.daxen.mydancekmpsharedui.features.user.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daxen.mydancekmpsharedui.data.auth.repository.AuthRepository
import com.daxen.mydancekmpsharedui.data.user.model.User
import com.daxen.mydancekmpsharedui.data.user.repository.UserRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

internal class UserViewModel(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
): ViewModel() {
    val currentUser: StateFlow<User> = userRepository.currentUser

    fun signOut() {
        viewModelScope.launch {
            try {
                authRepository.logout()
                userRepository.logOut()
            }catch (e: Exception) {
                println("UserViewModel Error en register $e")
            }
        }
    }

}