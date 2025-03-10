package com.daxen.mydancekmpsharedui.domain.auth

import com.daxen.mydancekmpsharedui.data.auth.repository.AuthRepository

class LogOutUseCase (
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() {
        authRepository.logout()
    }
}