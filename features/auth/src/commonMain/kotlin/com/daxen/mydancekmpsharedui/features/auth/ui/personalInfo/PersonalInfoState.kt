package com.daxen.mydancekmpsharedui.features.auth.ui.personalInfo

sealed class PersonalInfoState {
    data object Idle : PersonalInfoState()
    data object Loading : PersonalInfoState()
    data object Success : PersonalInfoState()
    data class Error(val message: String) : PersonalInfoState()
}