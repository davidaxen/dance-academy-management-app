package com.daxen.mydancekmpsharedui.features.auth.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daxen.mydancekmpsharedui.domain.auth.LogOutUseCase
//import com.daxen.mydancekmpsharedui.data.auth.model.User
//import com.daxen.mydancekmpsharedui.data.auth.repository.AuthRepository
import com.daxen.mydancekmpsharedui.domain.auth.LoginUseCase
import com.daxen.mydancekmpsharedui.domain.auth.RegisterUseCase
//import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val logOutUseCase: LogOutUseCase,
) : ViewModel() {
    //val currentUser: StateFlow<User?> = authRepository.currentUser

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                println("entroooo")
//                authRepository.login(email.trim(), password.trim())
                loginUseCase(email.trim(), password.trim())
            }catch (e: Exception) {
                println("UserViewModel Error en login $e")
            }
        }
    }

    fun register(email: String, password: String) {
        viewModelScope.launch {
            try {
                registerUseCase(email.trim(), password.trim())
            }catch (e: Exception) {
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

    fun logout() {
        viewModelScope.launch {
            try {
                logOutUseCase()
            } catch (e: Exception) {
                println("UserViewModel Error en logout $e")
            }
        }
    }
}