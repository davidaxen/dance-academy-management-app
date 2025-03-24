package com.daxen.mydancekmpsharedui.features.user.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person

object Constants {
    val profileItems = listOf(
        ProfileItem.Section("Tu cuenta"),
        ProfileItem.Option("Datos personales", Icons.Default.Person, ProfileAction.ViewPersonalInfo),
        ProfileItem.Option("Cambiar contraseña", Icons.Default.Lock, ProfileAction.ChangePassword),

//        ProfileItem.Section("Mis clases"),
//        ProfileItem.Option("Próximas clases", Icons.Default.Event, "upcoming_classes"),
//        ProfileItem.Option("Clases favoritas", Icons.Default.Favorite, "favorite_classes"),

//        ProfileItem.Section("Pagos"),
//        ProfileItem.Option("Métodos de pago", Icons.Default.CreditCard, "payment_methods"),
//        ProfileItem.Option("Suscripciones", Icons.Default.Star, "subscriptions"),

//        ProfileItem.Section("Mi academia"),
//        ProfileItem.Option("Mi academia", Icons.Default.School, "my_academy"),
//        ProfileItem.Option("Mi perfil profesional", Icons.Default.Info, "bio"),

        ProfileItem.Section("Preferencias"),
        ProfileItem.Option("Notificaciones", Icons.Default.Notifications, ProfileAction.Notifications),
//        ProfileItem.Option("WhatsApp", Icons.Default.Chat, "whatsapp"),

        ProfileItem.Section("Ajustes"),
//        ProfileItem.Option("Ayuda", Icons.Default.Info, "help"),
        ProfileItem.Option("Idioma", Icons.Default.Edit, ProfileAction.ViewPersonalInfo),
        ProfileItem.Option("Cerrar sesión", Icons.AutoMirrored.Filled.ExitToApp, ProfileAction.Logout)
    )

}