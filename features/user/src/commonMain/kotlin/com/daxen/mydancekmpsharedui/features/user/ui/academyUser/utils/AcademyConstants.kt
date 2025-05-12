package com.daxen.mydancekmpsharedui.features.user.ui.academyUser.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.School
import com.daxen.mydancekmpsharedui.features.user.AcademyProfileAction
import com.daxen.mydancekmpsharedui.features.user.utils.AcademyProfileItem

object AcademyConstants {
    val AcademyProfileItems = listOf(
        AcademyProfileItem.Section("Tu academia"),
        AcademyProfileItem.Option("Datos de la academia", Icons.Default.School, AcademyProfileAction.AcademyInfoRoute),
        
        AcademyProfileItem.Section("Cuenta"),
        AcademyProfileItem.Option("Cambiar contraseña", Icons.Default.Lock),

        AcademyProfileItem.Section("Preferencias"),
        AcademyProfileItem.Option("Notificaciones", Icons.Default.Notifications),
        AcademyProfileItem.Option("Personalización", Icons.Default.Edit, AcademyProfileAction.CustomizationRoute),

        AcademyProfileItem.Section("Ajustes"),
    )
} 