package com.daxen.mydancekmpsharedui.features.auth.ui.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.daxen.mydancekmpsharedui.core.ui.LocalPadding
import com.daxen.mydancekmpsharedui.core.ui.theme.*
import com.daxen.mydancekmpsharedui.features.auth.ui.components.AuthButton
import com.daxen.mydancekmpsharedui.features.auth.ui.components.AuthCard
import com.daxen.mydancekmpsharedui.features.auth.ui.components.AuthTitleAndSubtitle
import com.daxen.mydancekmpsharedui.features.auth.ui.components.CardTextButton
import com.daxen.mydancekmpsharedui.features.auth.ui.components.CurvedBackground
import com.daxen.mydancekmpsharedui.features.auth.ui.components.CustomTextField
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

    var email by remember { mutableStateOf("DA@gmail.com") }
    var password by remember { mutableStateOf("123456") }
    var confirmPassword by remember { mutableStateOf("123456") }
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

            Button(navigateToRoleSelection) {
                Text("asdasd")
            }
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = LocalPadding.current.large),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(60.dp))

                AuthTitleAndSubtitle(
                    title = "Crear cuenta",
                    subtitle = "Regístrate para comenzar"
                )

                Spacer(modifier = Modifier.height(48.dp))

                AuthCard {
                    CustomTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            viewModel.resetErrors()
                        },
                        label = "Correo electrónico",
                        error = emailError,
                        leadingIcon = Icons.Default.Email,
                        leadingIconDescription = "Email",
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next,
                    )

                    CustomTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            viewModel.resetErrors()
                        },
                        label = "Contraseña",
                        error = passwordError,
                        leadingIcon = Icons.Default.Lock,
                        leadingIconDescription = "Contraseña",
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next,
                    )

                    CustomTextField(
                        value = confirmPassword,
                        onValueChange = {
                            confirmPassword = it
                            viewModel.resetErrors()
                        },
                        label = "Repetir contraseña",
                        error = confirmPasswordError,
                        leadingIcon = Icons.Default.Lock,
                        leadingIconDescription = "Repetir contraseña",
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next,
                    )

                    Spacer(modifier = Modifier.height(4.dp))

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
                CardTextButton(
                    text = "¿Ya tienes cuenta? Inicia sesión",
                    onClick = navigateToLogin
                )
            }
        }
    }
}
