package com.daxen.mydancekmpsharedui.features.auth.ui.register

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daxen.mydancekmpsharedui.core.ui.theme.*
import com.daxen.mydancekmpsharedui.features.auth.ui.components.CurvedBackground
import com.daxen.mydancekmpsharedui.features.auth.ui.components.EmailField
import com.daxen.mydancekmpsharedui.features.auth.ui.components.PasswordField

@Composable
internal fun RegisterScreen(
    viewModel: RegisterViewModel,
    navigateToLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    val registerState by viewModel.registerState.collectAsState()
    val emailError by viewModel.emailError.collectAsState()
    val passwordError by viewModel.passwordError.collectAsState()
    val confirmPasswordError by viewModel.confirmPasswordError.collectAsState()
    val isRegistering by viewModel.isRegistering.collectAsState()
    var isNavigating by remember { mutableStateOf(false) }
    
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }

    LaunchedEffect(registerState) {
        if (registerState is RegisterState.Success && !isNavigating) {
            isNavigating = true
            navigateToLogin()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        CurvedBackground(modifier)
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top Bar con flecha de navegación
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = navigateToLogin,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = Color.White
                    )
                }
            }
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(60.dp))
                Text(
                    text = "Crear cuenta",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Regístrate para comenzar",
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

                        Spacer(modifier = Modifier.height(16.dp))

                        PasswordField(
                            password = confirmPassword,
                            onPasswordChange = { 
                                confirmPassword = it
                                viewModel.resetErrors()
                            },
                            showPassword = showConfirmPassword,
                            onShowPasswordToggle = { showConfirmPassword = !showConfirmPassword },
                            error = confirmPasswordError,
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        when (registerState) {
                            is RegisterState.Error -> {
                                Text(
                                    text = (registerState as RegisterState.Error).message,
                                    color = MaterialTheme.colorScheme.error,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(
                                    onClick = { /* TODO: Implementar registro */ },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(56.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = PrimaryBlue,
                                        contentColor = MaterialTheme.colorScheme.onPrimary
                                    )
                                ) {
                                    Text(
                                        text = "Registrarse",
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                            }
                            else -> {
                                Button(
                                    onClick = { viewModel.register(email, password, confirmPassword) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(56.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = PrimaryBlue,
                                        contentColor = MaterialTheme.colorScheme.onPrimary
                                    ),
                                    enabled = !isRegistering
                                ) {
                                    if (isRegistering) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(24.dp),
                                            color = MaterialTheme.colorScheme.onPrimary,
                                            strokeWidth = 2.dp
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                    }
                                    Text(
                                        text = if (isRegistering) "Registrando..." else "Registrarse",
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        TextButton(
                            onClick = navigateToLogin,
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = PrimaryBlue
                            ),
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            Text(
                                text = "¿Ya tienes cuenta? Inicia sesión",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}

