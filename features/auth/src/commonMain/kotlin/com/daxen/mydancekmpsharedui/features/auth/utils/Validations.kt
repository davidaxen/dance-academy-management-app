package com.daxen.mydancekmpsharedui.features.auth.utils

object Validations {
    private fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")
        return emailRegex.matches(email)
    }

    fun validateEmail(email: String): String? {
        return when {
            email.isEmpty() -> "Por favor, introduce un correo válido"
            !isValidEmail(email) -> "El formato del correo no es válido"
            else -> null
        }
    }

    fun validatePassword(password: String): String? {
        return when {
            password.isEmpty() -> "Por favor, introduce tu contraseña"
            else -> null
        }
    }
} 