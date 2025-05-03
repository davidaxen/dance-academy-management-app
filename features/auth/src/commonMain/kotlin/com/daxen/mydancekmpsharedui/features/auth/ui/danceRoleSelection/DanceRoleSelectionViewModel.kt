package com.daxen.mydancekmpsharedui.features.auth.ui.danceRoleSelection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daxen.mydancekmpsharedui.data.user.model.DanceRole
import com.daxen.mydancekmpsharedui.data.user.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DanceRoleSelectionViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _selectedRole = MutableStateFlow<DanceRole?>(null)
    val selectedRole: StateFlow<DanceRole?> = _selectedRole.asStateFlow()

    private val _danceRoleSelectionState = MutableStateFlow<DanceRoleSelectionState>(DanceRoleSelectionState.Idle)
    val danceRoleSelectionState: StateFlow<DanceRoleSelectionState> = _danceRoleSelectionState.asStateFlow()

    fun onRoleSelected(role: DanceRole) {
        _selectedRole.value = role
    }

    fun saveDanceRole() {
        viewModelScope.launch {
            _danceRoleSelectionState.value = DanceRoleSelectionState.Loading
            if (_selectedRole.value != null) {
                userRepository.setDanceRole(_selectedRole.value!!)
                userRepository.saveUserToDatabase()
                _danceRoleSelectionState.value = DanceRoleSelectionState.Success
            } else {
                _danceRoleSelectionState.value = DanceRoleSelectionState.Error("Error al guardar el usuario, vuelva a intentarlo.")
            }
        }
    }
} 