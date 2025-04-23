package com.daxen.mydancekmpsharedui.features.auth.ui.userRoleSelection

import androidx.lifecycle.ViewModel
import com.daxen.mydancekmpsharedui.data.user.model.UserRole
import com.daxen.mydancekmpsharedui.data.user.repository.UserRepository

class UserRoleSelectionViewModel(
    private val userRepository: UserRepository
): ViewModel() {
    fun onRoleSelected(role: UserRole) {
        userRepository.setRole(role)
    }
}