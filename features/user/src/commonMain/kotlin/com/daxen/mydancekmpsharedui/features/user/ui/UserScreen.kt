package com.daxen.mydancekmpsharedui.features.user.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.daxen.mydancekmpsharedui.core.ui.LocalPadding
import com.daxen.mydancekmpsharedui.data.user.model.User
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun UserScreen(
    viewModel: UserViewModel = koinViewModel(),
    modifier: Modifier = Modifier,
    navigateToLogin: () -> Unit
) {
    val userState by viewModel.userState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(LocalPadding.current.normal),
        contentAlignment = Alignment.Center
    ) {
        when (userState) {
            is UserUiState.Loading -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Cargando usuario...")
                }
            }

            is UserUiState.Success -> {
                val user = (userState as UserUiState.Success).user
                UserProfile(user, signOut = {
                    viewModel.signOut()
                    navigateToLogin()
                })
            }

            is UserUiState.Error -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Error al cargar el usuario",
                        color = Color.Red,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { viewModel.reloadUser() }) {
                        Text("Reintentar")
                    }
                }
            }
        }
    }
}

@Composable
fun UserProfile(user: User, signOut: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Hola, ${user.name}")
        Text(text = "Email: ${user.email}")

        Button(onClick = signOut) {
            Text("Desconectarse")
        }
    }
}