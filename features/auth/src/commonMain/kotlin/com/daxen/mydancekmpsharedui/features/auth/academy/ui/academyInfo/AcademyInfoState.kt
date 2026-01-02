package com.daxen.mydancekmpsharedui.features.auth.academy.ui.academyInfo

sealed class AcademyInfoState {
    data object Initial : AcademyInfoState()
    data object Loading : AcademyInfoState()
    data object Success : AcademyInfoState()
    data class Error(val message: String) : AcademyInfoState()
}