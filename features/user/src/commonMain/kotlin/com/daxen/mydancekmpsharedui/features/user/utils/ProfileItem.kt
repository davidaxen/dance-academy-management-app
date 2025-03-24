package com.daxen.mydancekmpsharedui.features.user.utils

import androidx.compose.ui.graphics.vector.ImageVector

sealed class ProfileItem {
    data class Section(val title: String) : ProfileItem()
    data class Option(val label: String, val icon: ImageVector, val onClick: ProfileAction) : ProfileItem()
}

sealed class ProfileAction {
    data object ViewPersonalInfo : ProfileAction()
    data object ChangePassword : ProfileAction()
    data object Logout : ProfileAction()
    data object Notifications : ProfileAction()
}