package com.daxen.mydancekmpsharedui.features.user.ui.academyUser

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daxen.mydancekmpsharedui.data.auth.repository.AuthRepository
import com.daxen.mydancekmpsharedui.data.user.model.UserAcademy
import com.daxen.mydancekmpsharedui.data.user.repository.AcademyUserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

internal class AcademyUserViewModel(
    private val academyUserRepository: AcademyUserRepository,
    private val authRepository: AuthRepository,
): ViewModel() {
    private val _userState = MutableStateFlow<AcademyUserUiState>(AcademyUserUiState.Loading)
    val userState: StateFlow<AcademyUserUiState> get() = _userState

    init {
        loadUser()
    }

    private fun loadUser() {
        viewModelScope.launch {
            try {
                academyUserRepository.currentAcademy.collect { user ->
                    if (user == UserAcademy.EMPTY) {
                        _userState.value = AcademyUserUiState.Loading
                    } else {
                        _userState.value = AcademyUserUiState.Success(user)
                    }
                }
            } catch (e: Exception) {
                _userState.value = AcademyUserUiState.Error
            }
        }
    }

    fun reloadUser() {
        _userState.value = AcademyUserUiState.Loading
        loadUser()
    }

    fun signOut() {
        viewModelScope.launch {
            try {
                authRepository.logout()
                academyUserRepository.logOut()
            }catch (e: Exception) {
                println("AcademyUserViewModel Error en register $e")
            }
        }
    }
} 