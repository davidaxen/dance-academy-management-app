package com.daxen.mydancekmpsharedui.features.user.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.Color
import com.daxen.mydancekmpsharedui.features.user.ProfileAction

object Constants {
    val profileItems = listOf(
        ProfileItem.Section("Tu cuenta"),
        ProfileItem.Option("Datos personales", Icons.Default.Person, ProfileAction.PersonalInfoRoute),
        ProfileItem.Option("Cambiar contraseña", Icons.Default.Lock),

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
        ProfileItem.Option("Notificaciones", Icons.Default.Notifications),
        ProfileItem.Option("Idioma", Icons.Default.Edit),
//        ProfileItem.Option("WhatsApp", Icons.Default.Chat, "whatsapp"),

        ProfileItem.Section("Ajustes"),
//        ProfileItem.Option("Ayuda", Icons.Default.Info, "help"),
        ProfileItem.Option("Borrar cuenta (Permanente)", Icons.Default.DeleteForever, ProfileAction.DeleteAccount, color = Color.Red),
    )

}