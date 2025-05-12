package com.daxen.mydancekmpsharedui.features.auth.academy.ui.subscription.utils

import com.daxen.mydancekmpsharedui.data.user.model.AddOnType
import com.daxen.mydancekmpsharedui.data.user.model.PlanType

object Constants {
    val planDescriptions = mapOf(
        PlanType.STARTER to "Starter (Gratis)",
        PlanType.PRO to "Pro (29€/mes)",
        PlanType.ELITE to "Elite (desde 99€/mes)"
    )
    val planDetails = mapOf(
        PlanType.STARTER to listOf(
            "1 profesor incluido",
            "30 alumnos activos",
            "Reservas de alumnos",
            "Sin subida de vídeos",
            "Sin gestión de pagos",
            "Soporte email (48h)",
        ),
        PlanType.PRO to listOf(
            "5 profesores incluidos",
            "200 alumnos activos",
            "Reservas de alumnos",
            "Subida de vídeos (50GB)",
            "Gestión de pagos y bonos",
            "Soporte email (24h)",
        ),
        PlanType.ELITE to listOf(
            "Profesores ilimitados",
            "Alumnos ilimitados",
            "Reservas de alumnos",
            "Subida de vídeos (200GB+)",
            "Gestión de pagos y bonos",
            "Personalizacion (colores, diseño...)",
            "Soporte dedicado (chat o WhatsApp)",
        )
    )
    val availableAddons = listOf(
        AddOnType.EXTRA_PROFESSOR to "Profesor adicional (+2€/profesor)",
        AddOnType.EXTRA_ALUMNOS_50 to "Alumno adicional (bloque de 50) (+3€)",
        AddOnType.EXTRA_STORAGE_50GB to "Almacenamiento extra (50GB) (+5€)",
    )

    // Emojis e info visual para los planes
    val planEmojis = mapOf(
        PlanType.STARTER to "🎓",
        PlanType.PRO to "⭐",
        PlanType.ELITE to "👑"
    )
}