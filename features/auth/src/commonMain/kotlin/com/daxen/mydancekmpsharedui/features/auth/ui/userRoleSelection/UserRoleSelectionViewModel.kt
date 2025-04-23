package com.daxen.mydancekmpsharedui.features.auth.ui.userRoleSelection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daxen.mydancekmpsharedui.data.auth.repository.AuthRepository
import com.daxen.mydancekmpsharedui.data.user.model.UserRole
import com.daxen.mydancekmpsharedui.data.user.repository.UserRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserRoleSelectionViewModel(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
): ViewModel() {
    val currentEmail: StateFlow<String?> = authRepository.currentEmail

    fun onRoleSelected(role: UserRole) {
        userRepository.setRole(role)
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}