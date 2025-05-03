package com.daxen.mydancekmpsharedui.features.auth.ui.login

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
import com.daxen.mydancekmpsharedui.features.auth.AuthViewModel
import com.daxen.mydancekmpsharedui.features.auth.PostSplashDestination
import com.daxen.mydancekmpsharedui.features.auth.ui.components.CurvedBackground
import com.daxen.mydancekmpsharedui.features.auth.ui.components.AuthButton
import com.daxen.mydancekmpsharedui.features.auth.ui.components.AuthCard
import com.daxen.mydancekmpsharedui.features.auth.ui.components.AuthTitleAndSubtitle
import com.daxen.mydancekmpsharedui.features.auth.ui.components.CardTextButton
import com.daxen.mydancekmpsharedui.features.auth.ui.components.CustomTextField

@Composable
internal fun LoginScreen(
    viewModel: LoginViewModel,
    checkerViewModel: AuthViewModel,
    navigateToAcademySelection: () -> Unit,
    navigateToRoleSelection: () -> Unit,
    navigateToRegister: () -> Unit,
    modifier: Modifier = Modifier
) {
    val loginState by viewModel.loginState.collectAsState()
    val emailError by viewModel.emailError.collectAsState()
    val passwordError by viewModel.passwordError.collectAsState()
    val isLoggingIn by viewModel.isLoggingIn.collectAsState()
    val afterLoginDestination by checkerViewModel.afterLoginDestination.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isNavigating by remember { mutableStateOf(false) }

    LaunchedEffect(loginState) {
        if (loginState is LoginState.Success && !isNavigating) {
            isNavigating = true
            checkerViewModel.checkUserStateAfterLogin()
        }
    }

    LaunchedEffect(afterLoginDestination) {
        when (afterLoginDestination) {
            PostSplashDestination.CompleteProfile -> {
                navigateToRoleSelection()
            }
            PostSplashDestination.AcademySelection -> {
                navigateToAcademySelection()
            }
            else -> {
                isNavigating = false
                return@LaunchedEffect
            }
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
                .padding(horizontal = LocalPadding.current.large),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(116.dp))

            AuthTitleAndSubtitle(
                title = "Iniciar sesión",
                subtitle = "Accede a tu cuenta para continuar"
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
                    imeAction = ImeAction.Done,
                )

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
            CardTextButton(
                text = "¿No tienes cuenta? Regístrate",
                onClick = navigateToRegister
            )
        }
    }
}





