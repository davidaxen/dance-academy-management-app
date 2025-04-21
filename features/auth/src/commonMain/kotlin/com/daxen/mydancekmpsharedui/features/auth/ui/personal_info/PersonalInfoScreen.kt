package com.daxen.mydancekmpsharedui.features.auth.ui.personal_info

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.daxen.mydancekmpsharedui.core.ui.LocalPadding
import com.daxen.mydancekmpsharedui.core.ui.theme.*
import com.daxen.mydancekmpsharedui.features.auth.ui.components.*

@Composable
internal fun PersonalInfoScreen(
    viewModel: PersonalInfoViewModel,
    onNavigateBack: () -> Unit,
    onNavigateNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.personalInfoState.collectAsState()
    val firstName by viewModel.firstName.collectAsState()
    val firstNameError by viewModel.firstNameError.collectAsState()
    val lastName by viewModel.lastName.collectAsState()
    val lastNameError by viewModel.lastNameError.collectAsState()
    val birthDateError by viewModel.birthDateError.collectAsState()
    val phone by viewModel.phone.collectAsState()
    val phoneError by viewModel.phoneError.collectAsState()
    val selectedPrefix by viewModel.selectedPrefix.collectAsState()
    val isSubmitting by viewModel.isSubmitting.collectAsState()

    var expanded by remember { mutableStateOf(false) }
    var isNavigating by remember { mutableStateOf(false) }

    LaunchedEffect(state) {
        if (state is PersonalInfoState.Success && !isNavigating) {
            isNavigating = true
//            onNavigateNext()
        }
    }

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
                        onValueChange = { viewModel.updateFirstName(it) },
                        label = "Nombre",
                        error = firstNameError,
                        keyboardType = KeyboardType.Text,
                        leadingIcon = Icons.Default.Person,
                        leadingIconDescription = "Nombre"
                    )

                    CustomTextField(
                        value = lastName,
                        onValueChange = { viewModel.updateLastName(it) },
                        label = "Apellidos",
                        error = lastNameError,
                        keyboardType = KeyboardType.Text,
                        leadingIcon = Icons.Default.Person,
                        leadingIconDescription = "Apellidos"
                    )

                    DatePickerField(
                        onValueChange = {
                            viewModel.updateBirthDate(it)
                            print(birthDateError)
                                        },
                        error = birthDateError,
                    )

                    CustomTextField(
                        value = phone,
                        onValueChange = { viewModel.updatePhone(it) },
                        label = "Teléfono",
                        error = phoneError,
                        keyboardType = KeyboardType.Phone,
                        customLeadingIcon = {
                            CountryCodeSelectable(
                                onSelected = { viewModel.updateSelectedPrefix(it) },
                                onClick = { expanded = true },
                                onDismiss = { expanded = false },
                                error = phoneError,
                                expanded = expanded,
                                selectedPrefix = selectedPrefix,
                                prefixOptions = viewModel.prefixOptions
                            )
                        },
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    when (state) {
                        is PersonalInfoState.Error -> {
                            Text(
                                text = (state as PersonalInfoState.Error).message,
                                color = MaterialTheme.colorScheme.error,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            AuthButton(
                                text = "Continuar",
                                onClick = { viewModel.validateAndSubmit() },
                                isLoading = false
                            )
                        }
                        else -> {
                            AuthButton(
                                text = "Continuar",
                                onClick = { viewModel.validateAndSubmit() },
                                isLoading = isSubmitting
                            )
                        }
                    }
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
    error: String?,
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
            Text(
                text = selectedPrefix,
                color = if (error != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onPrimaryContainer
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Seleccionar prefijo",
                tint = if (error != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onPrimaryContainer
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