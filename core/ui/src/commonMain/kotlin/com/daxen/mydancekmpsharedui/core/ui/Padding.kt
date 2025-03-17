package com.daxen.mydancekmpsharedui.core.ui

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Padding(
    val tiny: Dp = 8.dp,
    val small: Dp = 12.dp,
    val normal: Dp = 16.dp,
    val big: Dp = 24.dp,
    val large: Dp = 32.dp,
)

val LocalPadding = compositionLocalOf { Padding() }