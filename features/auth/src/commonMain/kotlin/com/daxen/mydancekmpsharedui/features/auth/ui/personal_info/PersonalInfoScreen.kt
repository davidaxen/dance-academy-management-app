package com.daxen.mydancekmpsharedui.features.auth.ui.personal_info

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.daxen.mydancekmpsharedui.core.ui.theme.*
import com.daxen.mydancekmpsharedui.features.auth.ui.components.*

@Composable
internal fun PersonalInfoScreen(
    onNavigateBack: () -> Unit,
    onNavigateNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    var firstName by remember { mutableStateOf("") }
    var lastName1 by remember { mutableStateOf("") }
    var lastName2 by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        CurvedBackground(modifier)
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopBarBackSection(onNavigateBack)
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(60.dp))
                
                AuthTitleAndSubtitle(
                    title = "Información personal",
                    subtitle = "Completa tus datos para continuar"
                )

                Spacer(modifier = Modifier.height(48.dp))

                AuthCard {
                    PersonalInfoField(
                        value = firstName,
                        onValueChange = { firstName = it },
                        label = "Nombre",
                        keyboardType = KeyboardType.Text,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Nombre"
                            )
                        }
                    )

                    PersonalInfoField(
                        value = lastName1,
                        onValueChange = { lastName1 = it },
                        label = "Primer apellido",
                        keyboardType = KeyboardType.Text,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Primer apellido"
                            )
                        }
                    )

                    PersonalInfoField(
                        value = lastName2,
                        onValueChange = { lastName2 = it },
                        label = "Segundo apellido",
                        keyboardType = KeyboardType.Text,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Segundo apellido"
                            )
                        }
                    )

                    DatePickerField(
                        onValueChange = { birthDate = it }
                    )

                    PersonalInfoField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = "Teléfono",
                        keyboardType = KeyboardType.Phone,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Phone,
                                contentDescription = "Teléfono"
                            )
                        },
                        prefix = "+34"
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    AuthButton(
                        text = "Continuar",
                        onClick = onNavigateNext,
                        isLoading = false
                    )
                }
            }
        }
    }
}

@Composable
private fun TopBarBackSection(navigateBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, start = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = navigateBack,
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Volver",
                tint = Color.White
            )
        }
    }
} 