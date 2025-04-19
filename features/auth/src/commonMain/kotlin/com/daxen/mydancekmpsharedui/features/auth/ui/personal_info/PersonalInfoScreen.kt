package com.daxen.mydancekmpsharedui.features.auth.ui.personal_info

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.daxen.mydancekmpsharedui.core.ui.LocalPadding
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
    var birthDate by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    val prefixOptions: List<String> = listOf("+34", "+1", "+58", "+52", "+33")
    var expanded by remember { mutableStateOf(false) }
    var selectedPrefix by remember { mutableStateOf(prefixOptions.first()) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        CurvedBackgroundFull()
        Column(modifier = Modifier.fillMaxSize()) {
            TopBarBackSection(onNavigateBack)
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = LocalPadding.current.large),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AuthTitleAndSubtitle(
                    title = "Información",
                    subtitle = "Completa tu perfil para seguir adelante",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                AuthCard {
                    CustomTextField(
                        value = firstName,
                        onValueChange = { firstName = it },
                        label = "Nombre",
                        keyboardType = KeyboardType.Text,
                        leadingIcon = Icons.Default.Person,
                        leadingIconDescription = "Nombre"
                    )

                    CustomTextField(
                        value = lastName1,
                        onValueChange = { lastName1 = it },
                        label = "Apellidos",
                        keyboardType = KeyboardType.Text,
                        leadingIcon = Icons.Default.Person,
                        leadingIconDescription = "Apellidos"
                    )

                    DatePickerField(
                        onValueChange = { birthDate = it }
                    )

                    CustomTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = "Teléfono",
                        keyboardType = KeyboardType.Phone,
                        customLeadingIcon = {
                            CountryCodeSelectable(
                                onSelected = { selectedPrefix = it },
                                onClick = { expanded = true },
                                onDismiss = { expanded = false },
                                expanded = expanded,
                                selectedPrefix = selectedPrefix,
                                prefixOptions = prefixOptions
                            )
                        },
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    AuthButton(
                        text = "Continuar",
                        onClick = {},
                        isLoading = false
                    )
                }
            }
        }
    }
}

@Composable
private fun CountryCodeSelectable(
    onSelected: (String) -> Unit,
    onClick: () -> Unit,
    onDismiss: () -> Unit,
    expanded: Boolean,
    selectedPrefix: String,
    prefixOptions: List<String>
) {
    var searchQuery by remember { mutableStateOf("") }

    val filteredPrefixes = remember(searchQuery, prefixOptions) {
        prefixOptions.filter {
            it.contains(searchQuery) || it.contains(searchQuery, ignoreCase = true)
        }
    }

    Box{
        Row(
            modifier = Modifier
                .clickable { onClick() }
                .padding(start = 8.dp, end = 4.dp, top = 12.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = selectedPrefix)
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Seleccionar prefijo"
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismiss,
            containerColor = MaterialTheme.colorScheme.background,
        ) {
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Buscar código") },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                    focusedContainerColor = MaterialTheme.colorScheme.background,
                    unfocusedContainerColor = MaterialTheme.colorScheme.background,
                ),
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.bodyMedium,
            )

            filteredPrefixes.forEach { prefix ->
                DropdownMenuItem(
                    text = { Text(prefix, color = MaterialTheme.colorScheme.onBackground) },
                    onClick = { onSelected(prefix) }
                )
            }
        }
    }
}