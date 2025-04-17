package com.daxen.mydancekmpsharedui.features.auth.ui.login

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daxen.mydancekmpsharedui.core.ui.theme.*
import com.daxen.mydancekmpsharedui.features.auth.ui.components.EmailField
import com.daxen.mydancekmpsharedui.features.auth.ui.components.LoginButton
import com.daxen.mydancekmpsharedui.features.auth.ui.components.PasswordField

@Composable
internal fun LoginScreen(
    viewModel: LoginViewModel,
    navigateToUserScreen: () -> Unit,
    navigateToRegister: () -> Unit,
    modifier: Modifier = Modifier
) {
    val loginState by viewModel.loginState.collectAsState()
    val emailError by viewModel.emailError.collectAsState()
    val passwordError by viewModel.passwordError.collectAsState()
    val isLoggingIn by viewModel.isLoggingIn.collectAsState()
    var isNavigating by remember { mutableStateOf(false) }
    var showPassword by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

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
            
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = SurfaceLight
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    EmailField(
                        email = email,
                        onEmailChange = { 
                            email = it
                            viewModel.resetErrors()
                        },
                        error = emailError
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    PasswordField(
                        password = password,
                        onPasswordChange = { 
                            password = it
                            viewModel.resetErrors()
                        },
                        showPassword = showPassword,
                        onShowPasswordToggle = { showPassword = !showPassword },
                        error = passwordError
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    when (loginState) {
                        is LoginState.Error -> {
                            Text(
                                text = (loginState as LoginState.Error).message,
                                color = MaterialTheme.colorScheme.error,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            LoginButton(
                                onClick = { viewModel.validateAndLogin(email, password) },
                                isLoading = false,
                            )
                        }
                        else -> {
                            LoginButton(
                                onClick = { viewModel.validateAndLogin(email, password) },
                                isLoading = isLoggingIn,
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    TextButton(
                        onClick = navigateToRegister,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = PrimaryBlue
                        ),
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        Text(
                            text = "¿No tienes cuenta? Regístrate",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
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
