package com.daxen.mydancekmpsharedui.features.user.ui.academyUser

import com.daxen.mydancekmpsharedui.data.user.model.UserAcademy

sealed class AcademyUserUiState {
    data object Loading : AcademyUserUiState()
    data class Success(val user: UserAcademy) : AcademyUserUiState()
    data object Error : AcademyUserUiState()
} 