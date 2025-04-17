package com.daxen.mydancekmpsharedui.features.auth.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import com.daxen.mydancekmpsharedui.core.ui.theme.PrimaryBlue
import com.daxen.mydancekmpsharedui.core.ui.theme.PrimaryBlueLight
import com.daxen.mydancekmpsharedui.core.ui.theme.SecondaryPurple

@Composable
fun CurvedBackground(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f)
    ) {
        Canvas(
            modifier = modifier.fillMaxSize()
        ) {
            // Primera curva (principal)
            val mainPath = Path().apply {
                moveTo(0f, size.height * 0.7f)
                cubicTo(
                    size.width * 0.3f, size.height * 0.3f,
                    size.width * 0.7f, size.height * 1.2f,
                    size.width, size.height * 0.8f
                )
                lineTo(size.width, 0f)
                lineTo(0f, 0f)
                close()
            }

            // Segunda curva (detalle)
            val detailPath = Path().apply {
                moveTo(0f, size.height * 0.5f)
                cubicTo(
                    size.width * 0.2f, size.height * 0.2f,
                    size.width * 0.5f, size.height * 0.8f,
                    size.width, size.height * 0.4f
                )
                lineTo(size.width, 0f)
                lineTo(0f, 0f)
                close()
            }

            // Tercera curva (accent)
            val accentPath = Path().apply {
                moveTo(0f, size.height * 0.3f)
                cubicTo(
                    size.width * 0.1f, size.height * 0.1f,
                    size.width * 0.3f, size.height * 0.4f,
                    size.width, size.height * 0.2f
                )
                lineTo(size.width, 0f)
                lineTo(0f, 0f)
                close()
            }

            // Dibujar las curvas con diferentes gradientes
            drawPath(
                path = mainPath,
                brush = Brush.linearGradient(
                    colors = listOf(PrimaryBlue, SecondaryPurple),
                    start = Offset(0f, 0f),
                    end = Offset(size.width, size.height * 1.2f)
                )
            )

            drawPath(
                path = detailPath,
                brush = Brush.linearGradient(
                    colors = listOf(SecondaryPurple.copy(alpha = 0.7f), PrimaryBlueLight.copy(alpha = 0.7f)),
                    start = Offset(0f, 0f),
                    end = Offset(size.width, size.height * 0.8f)
                )
            )

            drawPath(
                path = accentPath,
                brush = Brush.linearGradient(
                    colors = listOf(PrimaryBlueLight.copy(alpha = 0.5f), PrimaryBlue.copy(alpha = 0.5f)),
                    start = Offset(0f, 0f),
                    end = Offset(size.width, size.height * 0.4f)
                )
            )

            // Añadir algunos círculos decorativos
            drawCircle(
                color = PrimaryBlue.copy(alpha = 0.2f),
                radius = size.width * 0.2f,
                center = Offset(size.width * 0.2f, size.height * 0.3f)
            )

            drawCircle(
                color = SecondaryPurple.copy(alpha = 0.2f),
                radius = size.width * 0.15f,
                center = Offset(size.width * 0.8f, size.height * 0.2f)
            )
        }
    }
}