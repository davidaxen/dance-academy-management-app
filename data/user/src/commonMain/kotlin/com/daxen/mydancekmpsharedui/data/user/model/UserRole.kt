package com.daxen.mydancekmpsharedui.data.user.model

enum class UserRole(val roleName: String) {
    ACADEMY("academy"),
    TEACHER("teacher"),
    STUDENT("student");

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
    }
}