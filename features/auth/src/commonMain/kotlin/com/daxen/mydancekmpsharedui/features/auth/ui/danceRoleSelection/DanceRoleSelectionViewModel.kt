package com.daxen.mydancekmpsharedui.features.auth.ui.danceRoleSelection

import androidx.lifecycle.ViewModel
import com.daxen.mydancekmpsharedui.data.user.model.DanceRole
import com.daxen.mydancekmpsharedui.data.user.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DanceRoleSelectionViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _selectedRole = MutableStateFlow<DanceRole?>(null)
    val selectedRole: StateFlow<DanceRole?> = _selectedRole.asStateFlow()

    fun onRoleSelected(role: DanceRole) {
        _selectedRole.value = role
    }

    fun saveDanceRole() {
        if (_selectedRole.value != null) {
            userRepository.setDanceRole(_selectedRole.value!!)
        }
    }
} 