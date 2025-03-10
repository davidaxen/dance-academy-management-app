package com.daxen.mydancekmpsharedui.data.auth.repository

import com.daxen.mydancekmpsharedui.core.firebase.auth.FirebaseAuthService

class AuthRepositoryImpl(
    private val firebaseAuthService: FirebaseAuthService,
): AuthRepository {

//    private val _currentUser = MutableStateFlow<User?>(null)
//    override val currentUser: StateFlow<User?> get() = _currentUser

    override suspend fun login(email: String, password: String) {
        val userReponse = firebaseAuthService.login(email, password)
//        _currentUser.value = userReponse.toUser()
    }

    override suspend fun register(email: String, password: String) {

    }

    override suspend fun logout() {
        firebaseAuthService.logout()
//        _currentUser.value = null
    }
}
