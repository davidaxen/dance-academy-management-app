package com.daxen.mydancekmpsharedui.core.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// Colores principales
val PrimaryBlue = Color(0xFF2196F3)  // Azul más vibrante
val PrimaryBlueLight = Color(0xFF64B5F6)
val PrimaryBlueDark = Color(0xFF1976D2)

// Colores secundarios
val SecondaryPurple = Color(0xFF9C27B0)
val SecondaryPurpleLight = Color(0xFFBA68C8)
val SecondaryPurpleDark = Color(0xFF7B1FA2)

// Colores de fondo
val BackgroundLight = Color(0xFFFFFFFF)
val BackgroundDark = Color(0xFF121212)

// Colores de superficie
val SurfaceLight = Color(0xFFFFFFFF)
val SurfaceDark = Color(0xFF1E1E1E)

// Colores de error
val ErrorLight = Color(0xFFB00020)
val ErrorDark = Color(0xFFCF6679)

// Paleta de colores para tema claro
val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = Color.White,
    primaryContainer = PrimaryBlueLight,
    onPrimaryContainer = Color.Black,
    secondary = SecondaryPurple,
    onSecondary = Color.White,
    secondaryContainer = SecondaryPurpleLight,
    onSecondaryContainer = Color.Black,
    background = BackgroundLight,
    onBackground = Color.Black,
    surface = SurfaceLight,
    onSurface = Color.Black,
    error = ErrorLight,
    onError = Color.White
)

// Paleta de colores para tema oscuro
val DarkColorScheme = darkColorScheme(
    primary = PrimaryBlue,
    onPrimary = Color.White,
    primaryContainer = PrimaryBlueDark,
    onPrimaryContainer = Color.White,
    secondary = SecondaryPurple,
    onSecondary = Color.White,
    secondaryContainer = SecondaryPurpleDark,
    onSecondaryContainer = Color.White,
    background = BackgroundDark,
    onBackground = Color.White,
    surface = SurfaceDark,
    onSurface = Color.White,
    error = ErrorDark,
    onError = Color.Black
) 