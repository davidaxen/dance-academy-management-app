package com.daxen.mydancekmpsharedui.features.auth.ui.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.daxen.mydancekmpsharedui.core.ui.theme.*
import com.daxen.mydancekmpsharedui.features.auth.ui.components.AuthButton
import com.daxen.mydancekmpsharedui.features.auth.ui.components.AuthCard
import com.daxen.mydancekmpsharedui.features.auth.ui.components.AuthTitleAndSubtitle
import com.daxen.mydancekmpsharedui.features.auth.ui.components.CardTextButton
import com.daxen.mydancekmpsharedui.features.auth.ui.components.CurvedBackground
import com.daxen.mydancekmpsharedui.features.auth.ui.components.EmailField
import com.daxen.mydancekmpsharedui.features.auth.ui.components.PasswordField
import com.daxen.mydancekmpsharedui.features.auth.ui.components.TopBarBackSection

@Composable
internal fun RegisterScreen(
    viewModel: RegisterViewModel,
    navigateToLogin: () -> Unit,
    navigateToRoleSelection: () -> Unit,
    modifier: Modifier = Modifier
) {
    val registerState by viewModel.registerState.collectAsState()
    val emailError by viewModel.emailError.collectAsState()
    val passwordError by viewModel.passwordError.collectAsState()
    val confirmPasswordError by viewModel.confirmPasswordError.collectAsState()
    val isRegistering by viewModel.isRegistering.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }
    var isNavigating by remember { mutableStateOf(false) }

    LaunchedEffect(registerState) {
        if (registerState is RegisterState.Success && !isNavigating) {
            isNavigating = true
            navigateToRoleSelection()
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
            TopBarBackSection(navigateToLogin)
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(60.dp))

                AuthTitleAndSubtitle(
                    title = "Crear cuenta",
                    subtitle = "Regístrate para comenzar"
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
                            AuthButton(
                                text = "Reintentar registro",
                                onClick = {  },
                                isLoading = false,
                            )
                        }
                        else -> {
                            AuthButton(
                                text = "Registrarse",
                                onClick = { viewModel.register(email, password, confirmPassword) },
                                isLoading = isRegistering,
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                CardTextButton(
                    text = "¿Ya tienes cuenta? Inicia sesión",
                    onClick = navigateToLogin
                )
            }
        }
    }
}
