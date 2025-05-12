package com.daxen.mydancekmpsharedui.features.user.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.daxen.mydancekmpsharedui.features.user.AcademyProfileAction
import com.daxen.mydancekmpsharedui.features.user.ProfileAction

sealed class ProfileItem {
    data class Section(val title: String) : ProfileItem()
    data class Option(
        val label: String,
        val icon: ImageVector,
        val onClick: ProfileAction? = null,
        val color: Color = Color.Unspecified
    ) : ProfileItem()
}

sealed class AcademyProfileItem {
    data class Section(val title: String) : AcademyProfileItem()
    data class Option(
        val label: String,
        val icon: ImageVector,
        val onClick: AcademyProfileAction? = null,
        val color: Color = Color.Unspecified
    ) : AcademyProfileItem()
}