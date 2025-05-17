package com.daxen.mydancekmpsharedui.data.user.model

enum class UserRole(val roleName: String) {
    ACADEMY("ACADEMY"),
    TEACHER("TEACHER"),
    STUDENT("STUDENT");

    fun toSpanishText(): String {
        return when(this) {
            ACADEMY -> "Academia"
            TEACHER -> "Profesor"
            STUDENT -> "Alumno"
        }
    }

    companion object {
        fun from(roleName: String?): UserRole? {
            return entries.find { it.roleName == roleName }
        }
        
        fun fromString(role: String): UserRole {
            return when (role.uppercase()) {
                "ACADEMY" -> ACADEMY
                "TEACHER" -> TEACHER
                else -> STUDENT
            }
        }
    }
}