package com.daxen.mydancekmpsharedui.data.auth.model

enum class UserRole(val roleName: String) {
    SCHOOL("school"),
    TEACHER("teacher"),
    STUDENT("student");

    companion object {
        fun from(roleName: String?): UserRole? {
            return entries.find { it.roleName == roleName }
        }
    }
}