package com.daxen.mydancekmpsharedui.features.auth.ui.role_selection

import androidx.lifecycle.ViewModel
import com.daxen.mydancekmpsharedui.data.user.repository.UserRepository

class DanceRoleSelectionViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    fun selectDanceRole(role: String) {
        if (role.isNotEmpty()) {
            userRepository.setDanceRole(role)
        }
    }
} 