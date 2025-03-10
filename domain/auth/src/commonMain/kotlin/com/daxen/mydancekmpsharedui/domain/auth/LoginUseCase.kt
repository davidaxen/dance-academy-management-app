package com.daxen.mydancekmpsharedui.domain.auth

import com.daxen.mydancekmpsharedui.data.auth.repository.AuthRepository

class LoginUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String) {
        authRepository.login(email, password)
    }
}