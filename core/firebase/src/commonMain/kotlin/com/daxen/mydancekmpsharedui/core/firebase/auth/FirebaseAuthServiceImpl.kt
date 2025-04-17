package com.daxen.mydancekmpsharedui.core.firebase.auth

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.firestore.firestore

class FirebaseAuthServiceImpl(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
) : FirebaseAuthService {

    override suspend fun login(email: String, password: String): String {

        return auth.signInWithEmailAndPassword(email, password).user?.uid
            ?: throw IllegalStateException("Error en autenticación")
//        val user = getUserData(result.user?.uid ?: throw IllegalStateException("Error en autenticación"))
    }

    override suspend fun register(email: String, password: String): String {
        return auth.createUserWithEmailAndPassword(email, password).user?.uid
            ?: throw IllegalStateException("Error creando usuario")
//        val user = User(
//            uid = result.user?.uid ?: throw IllegalStateException("Error creando usuario"),
//            email = email,
//            name = "",
//            role = UserRole.STUDENT // Rol por defecto
//        )
//        firestore.collection("users").document(user.uid).set(user).await()
//        return user
    }


    override fun getCurrentUserId(): String?{
        return auth.currentUser?.uid
    }

    override suspend fun logout() {
        auth.signOut()
    }


}