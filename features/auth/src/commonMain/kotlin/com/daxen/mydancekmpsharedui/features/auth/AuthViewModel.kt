package com.daxen.mydancekmpsharedui.features.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daxen.mydancekmpsharedui.data.auth.repository.AuthRepository
import com.daxen.mydancekmpsharedui.data.user.model.User
import com.daxen.mydancekmpsharedui.data.user.model.UserRole
import com.daxen.mydancekmpsharedui.data.user.repository.AcademyUserRepository
import com.daxen.mydancekmpsharedui.data.user.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val academyUserRepository: AcademyUserRepository
): ViewModel() {
    private val _startingDestination = MutableStateFlow<PostSplashDestination?>(null)
    val startingDestination: StateFlow<PostSplashDestination?> = _startingDestination

    fun checkStartingUserState() {
        viewModelScope.launch {
            if (!authRepository.isUserLoggedIn()) {
                _startingDestination.value = PostSplashDestination.Login
            } else {
                setLoginDestinationUserLogged(userRepository.getUserToCheck())
            }
        }
    }

    fun checkUserStateAfterLogin() {
        viewModelScope.launch {
            setLoginDestinationUserLogged(userRepository.getUserToCheck())
        }
    }

    private fun setLoginDestinationUserLogged(user: User) {
        viewModelScope.launch {
            if (user == User.EMPTY) {
                _startingDestination.value = PostSplashDestination.CompleteProfile
            } else {
                when (user.role) {
                    UserRole.ACADEMY -> {
                        userRepository.updateCurrentUser()
                        _startingDestination.value = PostSplashDestination.AcademyHome
                    }
                    UserRole.TEACHER -> {
                        userRepository.updateCurrentUser()
                        _startingDestination.value = PostSplashDestination.AcademySelection
                    }
                    UserRole.STUDENT -> {
//                        academyUserRepository.updateAcademyUser()
                        _startingDestination.value = PostSplashDestination.AcademyHome
                    }
                }
            }
        }
    }
}