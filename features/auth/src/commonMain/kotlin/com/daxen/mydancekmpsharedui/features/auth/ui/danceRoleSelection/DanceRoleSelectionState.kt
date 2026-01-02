package com.daxen.mydancekmpsharedui.features.auth.ui.danceRoleSelection

sealed class DanceRoleSelectionState {
    data object Idle : DanceRoleSelectionState()
    data object Loading : DanceRoleSelectionState()
    data object Success : DanceRoleSelectionState()
    data class Error(val message: String) : DanceRoleSelectionState()
}