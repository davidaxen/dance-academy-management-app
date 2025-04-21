package com.daxen.mydancekmpsharedui.features.auth.ui.role_selection

import androidx.lifecycle.ViewModel
import com.daxen.mydancekmpsharedui.data.user.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DanceRoleSelectionViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _selectedRole = MutableStateFlow<String?>(null)
    val selectedRole: StateFlow<String?> = _selectedRole.asStateFlow()

    fun onRoleSelected(role: String) {
        _selectedRole.value = role
    }

    fun saveDanceRole() {
        if (_selectedRole.value?.isNotEmpty() == true) {
            userRepository.setDanceRole(_selectedRole.value!!)
        }
    }
} 