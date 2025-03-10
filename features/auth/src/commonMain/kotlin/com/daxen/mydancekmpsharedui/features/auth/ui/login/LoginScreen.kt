package com.daxen.mydancekmpsharedui.features.auth.ui.login

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp

@Composable
internal fun LoginScreen(
    viewModel: LoginViewModel,
    navigateToUserScreen: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        Modifier.fillMaxSize()
    ) {
        CurvedBackground(modifier)
        FieldsSection(
            modifier,
            viewModel = viewModel,
            navigateToUserScreen = navigateToUserScreen
        )

    }
}


@Composable
private fun FieldsSection(modifier: Modifier = Modifier, viewModel: LoginViewModel, navigateToUserScreen: () -> Unit) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var user by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        TextField(value = user, onValueChange = {
            user = it
        }, label = {
            Text("Usuario")
        })
        Spacer(Modifier.size(20.dp))
        TextField(value = password, onValueChange = {
            password = it
        }, label = {
            Text("Contraseña")
        })

        Button(
            onClick = {
                viewModel.login(email = user, password = password)
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Blue,
                contentColor = Color.White
                )
        ) {
            Text("Iniciar")
        }

        Button(onClick = navigateToUserScreen) {
            Text("probando")
        }
    }
}

@Composable
private fun CurvedBackground(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.3f)
    ) {
        Canvas(
            modifier = modifier.fillMaxSize()
        ) {
            val path = Path().apply {
                moveTo(0f, size.height * 0.6f)
                cubicTo(
                    size.width * 0.4f, size.height * 0.2f, // Primer punto de control
                    size.width * 0.75f, size.height * 1.2f, // Segundo punto de control
                    size.width, size.height * 0.9f          // Fin de la curva
                )
                lineTo(size.width, 0f) // Línea hasta la esquina superior derecha
                lineTo(0f, 0f)         // Línea hasta la esquina superior izquierda
                close()                // Cierra el Path
            }

            drawPath(
                path = path,
                brush = Brush.linearGradient(
//                    colors = listOf(Primary, Secondary),
                    colors = listOf(Color.Blue, Color.Magenta),
//                    colors = listOf(Color(0xFF459DFF), Color(0xFF5CC8FF)),
//                    colors = listOf(Color(0xFFBB2525), Color(0xFF5C0B7F)),
                    start = Offset(0f, 0f),
                    end = Offset(size.width, size.height * 1.4f)
                )
            )
        }
    }
}