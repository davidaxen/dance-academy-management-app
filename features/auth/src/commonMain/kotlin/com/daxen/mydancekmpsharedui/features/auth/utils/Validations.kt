package com.daxen.mydancekmpsharedui.features.auth.utils

object Validations {
    private fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")
        return emailRegex.matches(email)
    }

    fun validateEmail(email: String): String? {
        return when {
            email.isEmpty() -> "Por favor, introduce un correo electrónico"
            !isValidEmail(email) -> "El formato del correo no es válido"
            else -> null
        }
    }

    fun validatePassword(password: String): String? {
        return when {
            password.isEmpty() -> "Por favor, introduce tu contraseña"
            password.length < 6 -> "La contraseña debe tener al menos 6 caracteres"
            else -> null
        }
    }

    fun validateConfirmPassword(password1: String, password2: String): String? {
        return when {
            password2.isEmpty() -> "Debes confirmar la contraseña"
            password2 != password1 -> "Las contraseñas no coinciden"
            else -> null
        }
    }
} 