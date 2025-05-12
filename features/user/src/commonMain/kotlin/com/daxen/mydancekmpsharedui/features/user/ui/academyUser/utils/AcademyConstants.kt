package com.daxen.mydancekmpsharedui.features.user.ui.academyUser.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.ui.graphics.Color
import com.daxen.mydancekmpsharedui.features.user.ProfileAction
import com.daxen.mydancekmpsharedui.features.user.utils.ProfileItem

object AcademyConstants {
    val profileItems = listOf(
        ProfileItem.Section("Tu academia"),
        ProfileItem.Option("Datos de la academia", Icons.Default.School, ProfileAction.PersonalInfoRoute),
        ProfileItem.Option("Gestionar profesores", Icons.Default.Person),
        ProfileItem.Option("Gestionar estudiantes", Icons.Default.Person),
        ProfileItem.Option("Gestionar horarios", Icons.Default.Schedule),
        
        ProfileItem.Section("Cuenta"),
        ProfileItem.Option("Cambiar contraseña", Icons.Default.Lock),

        ProfileItem.Section("Preferencias"),
        ProfileItem.Option("Notificaciones", Icons.Default.Notifications),
        ProfileItem.Option("Personalización", Icons.Default.Edit),

        ProfileItem.Section("Ajustes"),
        ProfileItem.Option("Borrar cuenta (Permanente)", Icons.Default.DeleteForever, ProfileAction.DeleteAccount, color = Color.Red),
    )
} 