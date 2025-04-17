package com.daxen.mydancekmpsharedui.data.auth.repository

import com.daxen.mydancekmpsharedui.core.firebase.auth.FirebaseAuthService
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow

class AuthRepositoryImpl(
    private val firebaseAuthService: FirebaseAuthService,
): AuthRepository {

    private val _currentUid = MutableStateFlow(firebaseAuthService.getCurrentUserId())
    override val currentUid: StateFlow<String?> get() = _currentUid

    override suspend fun login(email: String, password: String) {
        val currentUserAuthId = firebaseAuthService.login(email, password)
        _currentUid.value = currentUserAuthId
    }

    override suspend fun register(email: String, password: String) {
        val currentUserAuthId = firebaseAuthService.register(email, password)
        _currentUid.value = currentUserAuthId
    }

    override suspend fun logout() {
        firebaseAuthService.logout()
        _currentUid.value = null
    }
}
