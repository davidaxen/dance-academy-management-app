package com.daxen.mydancekmpsharedui.features.user.ui

import com.daxen.mydancekmpsharedui.data.user.model.User

sealed class UserUiState {
    data object Loading : UserUiState()
    data class Success(val user: User) : UserUiState()
    data object Error : UserUiState()
}
