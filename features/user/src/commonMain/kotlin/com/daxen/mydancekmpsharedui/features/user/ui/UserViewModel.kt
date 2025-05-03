package com.daxen.mydancekmpsharedui.features.user.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daxen.mydancekmpsharedui.data.auth.repository.AuthRepository
import com.daxen.mydancekmpsharedui.data.user.model.User
import com.daxen.mydancekmpsharedui.data.user.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

internal class UserViewModel(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
): ViewModel() {
    private val _userState = MutableStateFlow<UserUiState>(UserUiState.Loading)
    val userState: StateFlow<UserUiState> get() = _userState

    init {
        loadUser()
    }

    private fun loadUser() {
        viewModelScope.launch {
            try {
                userRepository.currentUser.collect { user ->
                    if (user == User.EMPTY) {
                        _userState.value = UserUiState.Loading
                    } else {
                        _userState.value = UserUiState.Success(user)
                    }
                }
            } catch (e: Exception) {
                _userState.value = UserUiState.Error
            }
        }
    }

    fun reloadUser() {
        _userState.value = UserUiState.Loading
        loadUser()
    }

    fun signOut() {
        viewModelScope.launch {
            try {
                authRepository.logout()
                userRepository.logOut()
            }catch (e: Exception) {
                println("UserViewModel Error en register $e")
            }
        }
    }

}