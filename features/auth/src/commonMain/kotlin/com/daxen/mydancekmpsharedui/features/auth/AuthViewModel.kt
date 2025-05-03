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
    private val _startingDestination = MutableStateFlow<PostSplashDestination?>(null)
    val startingDestination: StateFlow<PostSplashDestination?> = _startingDestination

    private val _afterLoginDestination = MutableStateFlow<PostSplashDestination?>(null)
    val afterLoginDestination: StateFlow<PostSplashDestination?> = _afterLoginDestination

    fun checkStartingUserState() {
        viewModelScope.launch {
            if (!authRepository.isUserLoggedIn()) {
                _startingDestination.value = PostSplashDestination.Login
            } else {
                if (!userRepository.isUserInfoComplete()) {
                    _startingDestination.value = PostSplashDestination.CompleteProfile
                } else {
                    _startingDestination.value = PostSplashDestination.AcademySelection
                }
            }
        }
    }

    fun checkUserStateAfterLogin() {
        viewModelScope.launch {
            if (!userRepository.isUserInfoComplete()) {
                _afterLoginDestination.value = PostSplashDestination.CompleteProfile
            } else {
                _afterLoginDestination.value = PostSplashDestination.AcademySelection
            }
        }
    }
}