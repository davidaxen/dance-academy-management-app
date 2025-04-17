package com.daxen.mydancekmpsharedui.features.auth.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daxen.mydancekmpsharedui.data.auth.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                _loginState.value = LoginState.Loading
                authRepository.login(email.trim(), password.trim())
                _loginState.value = LoginState.Success
            } catch (e: Exception) {
                println("LoginViewModel Error en login $e")
                _loginState.value = LoginState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    fun register(email: String, password: String) {
        viewModelScope.launch {
            try {
                authRepository.register(email.trim(), password.trim())
            } catch (e: Exception) {
                println("UserViewModel Error en register $e")
            }
        }
    }

//    fun getCurrentUser() {
//        viewModelScope.launch {
//            try {
//                val user = authRepository.getCurrentUser()
//                _userState.value = user
//            } catch (e: Exception) {
//                _errorState.value = e.message
//                Log.e("UserViewModel", "Error obteniendo usuario", e)
//            }
//        }
//    }

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

sealed class LoginState {
    data object Idle : LoginState()
    data object Loading : LoginState()
    data object Success : LoginState()
    data class Error(val message: String) : LoginState()
}