package com.daxen.mydancekmpsharedui.features.auth.ui.login

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daxen.mydancekmpsharedui.core.ui.theme.BackgroundLight
import com.daxen.mydancekmpsharedui.core.ui.theme.PrimaryBlue
import com.daxen.mydancekmpsharedui.core.ui.theme.PrimaryBlueLight
import com.daxen.mydancekmpsharedui.core.ui.theme.SecondaryPurple
import com.daxen.mydancekmpsharedui.core.ui.theme.SurfaceLight

@Composable
internal fun LoginScreen(
    viewModel: LoginViewModel,
    navigateToUserScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val loginState by viewModel.loginState.collectAsState()
    var isNavigating by remember { mutableStateOf(false) }

    LaunchedEffect(loginState) {
        if (loginState is LoginState.Success && !isNavigating) {
            isNavigating = true
            navigateToUserScreen()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        CurvedBackground(modifier)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(120.dp))
            Text(
                text = "Bienvenido",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "Inicia sesión para continuar",
                fontSize = 16.sp,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(48.dp))
            
            // Reemplazamos Surface por Card de Material 3
            androidx.compose.material3.Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                shape = RoundedCornerShape(16.dp),
                colors = androidx.compose.material3.CardDefaults.cardColors(
                    containerColor = SurfaceLight
                ),
                elevation = androidx.compose.material3.CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    FieldsSection(
                        viewModel = viewModel,
                        loginState = loginState,
                        isNavigating = isNavigating
                    )
                }
            }
        }
    }
}

@Composable
private fun FieldsSection(
    viewModel: LoginViewModel,
    loginState: LoginState,
    isNavigating: Boolean
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo electrónico") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Email",
                    tint = PrimaryBlue
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = PrimaryBlue,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Password",
                    tint = PrimaryBlue
                )
            },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = PrimaryBlue,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        when (loginState) {
            is LoginState.Error -> {
                Text(
                    text = loginState.message,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        viewModel.login(email = email, password = password)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = PrimaryBlue
                    )
                ) {
                    Text("Intentar de nuevo")
                }
            }
            else -> {
                Button(
                    onClick = {
                        viewModel.login(email = email, password = password)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = PrimaryBlue
                    ),
                    enabled = !isNavigating && loginState !is LoginState.Loading
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (loginState is LoginState.Loading || isNavigating) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        Text(if (loginState is LoginState.Loading || isNavigating) "Iniciando..." else "Iniciar sesión")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = { /* TODO: Implementar registro */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "¿No tienes cuenta? Regístrate",
                color = PrimaryBlue
            )
        }
    }
}

@Composable
private fun CurvedBackground(modifier: Modifier = Modifier) {
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