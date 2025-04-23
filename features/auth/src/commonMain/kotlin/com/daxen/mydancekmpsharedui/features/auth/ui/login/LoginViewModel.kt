package com.daxen.mydancekmpsharedui.features.auth.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daxen.mydancekmpsharedui.data.auth.repository.AuthRepository
import com.daxen.mydancekmpsharedui.features.auth.utils.Validations
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    private val _emailError = MutableStateFlow<String?>(null)
    val emailError: StateFlow<String?> = _emailError.asStateFlow()

    private val _passwordError = MutableStateFlow<String?>(null)
    val passwordError: StateFlow<String?> = _passwordError.asStateFlow()

    private val _isLoggingIn = MutableStateFlow(false)
    val isLoggingIn: StateFlow<Boolean> = _isLoggingIn.asStateFlow()

    fun validateAndLogin(email: String, password: String) {
        _emailError.value = Validations.validateEmail(email)
        _passwordError.value = Validations.validatePassword(password)

        if (_emailError.value == null && _passwordError.value == null) {
            _isLoggingIn.value = true
            login(email, password)
        } else {
            _isLoggingIn.value = false
        }
    }

    private fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                _loginState.value = LoginState.Loading
                authRepository.login(email.trim(), password.trim())
                _loginState.value = LoginState.Success
            } catch (e: Exception) {
                println("LoginViewModel Error en login $e")
                _loginState.value = LoginState.Error(e.message ?: "Error desconocido")
            }
            _isLoggingIn.value = false
        }
    }

    fun resetErrors() {
        _emailError.value = null
        _passwordError.value = null
    }

//    fun logout() {
//        viewModelScope.launch {
//            try {
//                authRepository.logout()
//            } catch (e: Exception) {
//                println("UserViewModel Error en logout $e")
//            }
//        }
//    }
}