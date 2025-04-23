package com.daxen.mydancekmpsharedui.features.auth.ui.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daxen.mydancekmpsharedui.data.auth.repository.AuthRepository
import com.daxen.mydancekmpsharedui.features.auth.utils.Validations
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var confirmPassword by mutableStateOf("")
        private set

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> = _registerState.asStateFlow()

    private val _emailError = MutableStateFlow<String?>(null)
    val emailError: StateFlow<String?> = _emailError.asStateFlow()

    private val _passwordError = MutableStateFlow<String?>(null)
    val passwordError: StateFlow<String?> = _passwordError.asStateFlow()

    private val _confirmPasswordError = MutableStateFlow<String?>(null)
    val confirmPasswordError: StateFlow<String?> = _confirmPasswordError.asStateFlow()

    private val _isRegistering = MutableStateFlow(false)
    val isRegistering: StateFlow<Boolean> = _isRegistering.asStateFlow()

    fun updateEmail(newEmail: String) {
        email = newEmail
        _emailError.value = null
    }
    fun updatePassword(newPassword: String) {
        password = newPassword
        _passwordError.value = null
    }
    fun updateConfirmPassword(newConfirmPassword: String) {
        confirmPassword = newConfirmPassword
        _confirmPasswordError.value = null
    }

    fun validateAndRegister() {
        _emailError.value = Validations.validateEmail(email)
        _passwordError.value = Validations.validatePassword(password)
        _confirmPasswordError.value = Validations.validateConfirmPassword(password, confirmPassword)

        if (_emailError.value == null && _passwordError.value == null && _confirmPasswordError.value == null) {
            _isRegistering.value = true
            register()
        } else {
            _isRegistering.value = false
        }
    }

    private fun register() {
        viewModelScope.launch {
            try {
                _registerState.value = RegisterState.Loading
                authRepository.register(email.trim(), password.trim())
                _registerState.value = RegisterState.Success
            } catch (e: Exception) {
                _registerState.value = RegisterState.Error(e.message ?: "Error desconocido")
            }
            _isRegistering.value = false
        }
    }
}