package com.daxen.mydancekmpsharedui.core.firebase.auth

import com.daxen.mydancekmpsharedui.core.firebase.auth.response.UserResponse
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.firestore.FirebaseFirestore

class FirebaseAuthServiceImpl(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
) : FirebaseAuthService {

    override suspend fun login(email: String, password: String): UserResponse {
        val result = auth.signInWithEmailAndPassword(email, password)
        val user = getUserData(result.user?.uid ?: throw IllegalStateException("Error en autenticación"))
        return user
    }

//    override suspend fun register(email: String, password: String): User {
//        val result = auth.createUserWithEmailAndPassword(email, password).await()
//        val user = User(
//            uid = result.user?.uid ?: throw IllegalStateException("Error creando usuario"),
//            email = email,
//            name = "",
//            role = UserRole.STUDENT // Rol por defecto
//        )
//        firestore.collection("users").document(user.uid).set(user).await()
//        return user
//    }


    override suspend fun getCurrentUser() {
//        val currentUser = auth.currentUser
//            ?: throw IllegalStateException("Usuario no autenticado")

//        val user = getUserData(currentUser.uid)
//        return user
    }

    override suspend fun logout() {
        auth.signOut()
    }

    private suspend fun getUserData(uid: String): UserResponse {
        val document = firestore.collection("users")
            .document(uid)
            .get()


        if (!document.exists) {
            throw IllegalStateException("No se encontraron datos del usuario")
        }

        val probando = document.data(UserResponse.serializer()).copy(uid = uid)

        println("prueba $probando")
        println("aquiiii ${document.id}")
        println("aquiiii2 ${auth.currentUser?.email} ${auth.currentUser?.uid}")

        return UserResponse()

//        return document.toObject(UserResponse::class.java)?.copy(uid = uid)
//            ?: throw IllegalStateException("Error al parsear los datos del usuario")
    }

}