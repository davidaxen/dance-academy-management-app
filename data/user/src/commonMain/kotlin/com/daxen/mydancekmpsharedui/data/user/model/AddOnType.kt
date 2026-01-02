package com.daxen.mydancekmpsharedui.data.user.model

enum class AddOnType(val addOnName: String) {
    EXTRA_PROFESSOR("extra_professor"),
    EXTRA_ALUMNOS_50("extra_students_50"),
    EXTRA_STORAGE_50GB("extra_storage_50gb");

    companion object {
        fun from(addOnName: String): AddOnType? {
            return AddOnType.entries.find { it.addOnName == addOnName }
        }
    }
}