package com.daxen.mydancekmpsharedui.features.auth.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.daxen.mydancekmpsharedui.features.auth.ui.components.AuthButton
import com.daxen.mydancekmpsharedui.features.auth.ui.components.AuthCard
import com.daxen.mydancekmpsharedui.features.auth.ui.components.AuthTitleAndSubtitle
import com.daxen.mydancekmpsharedui.features.auth.ui.components.CardTextButton
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

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
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

            AuthTitleAndSubtitle(
                title = "Iniciar sesión",
                subtitle = "Accede a tu cuenta para continuar"
            )

            Spacer(modifier = Modifier.height(48.dp))
            
            AuthCard {
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
                        AuthButton(
                            text = "Iniciar sesión",
                            onClick = { viewModel.validateAndLogin(email, password) },
                            isLoading = false,
                        )
                    }
                    else -> {
                        AuthButton(
                            text = "Iniciar sesión",
                            onClick = { viewModel.validateAndLogin(email, password) },
                            isLoading = isLoggingIn,
                        )
                    }
                }
                CardTextButton(
                    text = "¿Olvidaste tu contraseña?",
                    onClick = {}
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            CardTextButton(
                text = "¿No tienes cuenta? Regístrate",
                onClick = navigateToRegister
            )
        }
    }
}





