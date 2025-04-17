package com.daxen.mydancekmpsharedui.features.auth.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daxen.mydancekmpsharedui.data.auth.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class RegisterState {
    data object Idle : RegisterState()
    data object Loading : RegisterState()
    data object Success : RegisterState()
    data class Error(val message: String) : RegisterState()
}

class RegisterViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
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

    fun resetErrors() {
        _emailError.value = null
        _passwordError.value = null
        _confirmPasswordError.value = null
    }

    fun register(email: String, password: String, confirmPassword: String) {
        viewModelScope.launch {
            if (validateInputs(email, password, confirmPassword)) {
                try {
                    _isRegistering.value = true
                    _registerState.value = RegisterState.Loading
//                    authRepository.register(email, password)
                    _registerState.value = RegisterState.Success
                } catch (e: Exception) {
                    _registerState.value = RegisterState.Error(e.message ?: "Error desconocido")
                } finally {
                    _isRegistering.value = false
                }
            }
        }
    }

    private fun validateInputs(email: String, password: String, confirmPassword: String): Boolean {
        var isValid = true

        if (email.isBlank()) {
            _emailError.value = "El correo electrónico es obligatorio"
            isValid = false
        } else if (!email.matches(Regex("^[A-Za-z0-9+_.-]+@(.+)$"))) {
            _emailError.value = "El correo electrónico no es válido"
            isValid = false
        }

        if (password.isBlank()) {
            _passwordError.value = "La contraseña es obligatoria"
            isValid = false
        } else if (password.length < 6) {
            _passwordError.value = "La contraseña debe tener al menos 6 caracteres"
            isValid = false
        }

        if (confirmPassword.isBlank()) {
            _confirmPasswordError.value = "Debes confirmar la contraseña"
            isValid = false
        } else if (password != confirmPassword) {
            _confirmPasswordError.value = "Las contraseñas no coinciden"
            isValid = false
        }

        return isValid
    }
}