package com.daxen.mydancekmpsharedui.features.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daxen.mydancekmpsharedui.data.auth.repository.AuthRepository
import com.daxen.mydancekmpsharedui.data.user.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
): ViewModel() {
    private val _destination = MutableStateFlow<PostSplashDestination?>(null)
    val destination: StateFlow<PostSplashDestination?> = _destination

    fun checkUserState() {
        viewModelScope.launch {
            if (!authRepository.isUserLoggedIn()) {
                _destination.value = PostSplashDestination.Login
            } else {
                if (!userRepository.isUserInfoComplete()) {
                    _destination.value = PostSplashDestination.CompleteProfile
                } else {
                    _destination.value = PostSplashDestination.AcademySelection
                }
            }
        }
    }
}