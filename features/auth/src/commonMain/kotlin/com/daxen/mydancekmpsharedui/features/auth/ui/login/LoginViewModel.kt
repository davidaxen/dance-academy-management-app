package com.daxen.mydancekmpsharedui.features.auth.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

internal class LoginViewModel(
//    private val authRepository: AuthRepository
) : ViewModel() {
//    val currentUser: StateFlow<User?> = authRepository.currentUser

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
//                authRepository.login(email.trim(), password.trim())
            }catch (e: Exception) {
                println("UserViewModel Error en login $e")
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

    fun logout() {
        viewModelScope.launch {
            try {
//                authRepository.logout()
            } catch (e: Exception) {
                println("UserViewModel Error en logout $e")
            }
        }
    }
}