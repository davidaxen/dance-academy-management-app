package com.daxen.mydancekmpsharedui.core.firebase.auth

import dev.gitlive.firebase.auth.FirebaseAuth

class FirebaseAuthServiceImpl(
    private val auth: FirebaseAuth,
) : FirebaseAuthService {

    override suspend fun login(email: String, password: String): String {
        return auth.signInWithEmailAndPassword(email, password).user?.uid
            ?: throw IllegalStateException("Error en autenticación")
    }

    override suspend fun register(email: String, password: String): String {
        return auth.createUserWithEmailAndPassword(email, password).user?.uid
            ?: throw IllegalStateException("Error creando usuario")
    }

    override fun getCurrentUserId(): String?{
        return auth.currentUser?.uid
    }

    override fun getCurrentUserEmail(): String?{
        return auth.currentUser?.email
    }

    override suspend fun logout() {
        auth.signOut()
    }


}