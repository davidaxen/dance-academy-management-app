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

    fun validateFirstName(value: String): String? {
        return when {
            value.isBlank() -> "El nombre no puede estar vacío"
            else -> null
        }
    }

    fun validateLastName(value: String): String? {
        return when {
            value.isBlank() -> "El apellido no puede estar vacío"
            else -> null
        }
    }

    fun validateBirthDate(value: String): String? {
        return when {
            value.isBlank() -> "La fecha de nacimiento no puede estar vacía"
            else -> null
        }
    }

    fun validatePhone(value: String): String? {
        return when {
            value.isBlank() -> "El teléfono no puede estar vacío"
            value.length < 9 -> "El teléfono debe tener al menos 9 dígitos"
            else -> null
        }
    }
}

object AcademyValidations {
    fun validateName(name: String): String? {
        return when {
            name.isBlank() -> "El nombre es obligatorio"
            name.length < 3 -> "El nombre debe tener al menos 3 caracteres"
            else -> null
        }
    }

    fun validateNif(nif: String): String? {
        return when {
            nif.isBlank() -> "El NIF es obligatorio"
            !nif.matches(Regex("^[A-Z]\\d{8}$")) -> "El NIF debe tener el formato correcto (ej: A12345678)"
            else -> null
        }
    }

    fun validateAddress(address: String): String? {
        return when {
            address.isBlank() -> "La dirección es obligatoria"
            address.length < 5 -> "La dirección debe tener al menos 5 caracteres"
            else -> null
        }
    }

    fun validateOpeningTime(time: String): String? {
        return when {
            time.isBlank() -> "La hora de apertura es obligatoria"
            else -> null
        }
    }

    fun validateClosingTime(time: String): String? {
        return when {
            time.isBlank() -> "La hora de cierre es obligatoria"
            else -> null
        }
    }
} 