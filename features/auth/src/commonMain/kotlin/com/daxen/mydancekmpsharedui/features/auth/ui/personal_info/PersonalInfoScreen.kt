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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
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
    var lastName2 by remember { mutableStateOf("") }
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
                    subtitle = "Completa tus datos para continuar",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

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

@Composable
private fun CurvedBackgroundFull() {
    val progress = 1f
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f + (0.5f * progress))
    ) {
        // Primera curva (principal)
        val increment = 0.5f * progress
        val mainPath = Path().apply {
            val yStart = lerp(start = size.height * 0.7f, stop = size.height, fraction = progress)
            val controlY1 = lerp(start = size.height * 0.3f, stop = size.height, fraction = progress)
            val controlY2 = lerp(start = size.height * 1.2f, stop = size.height, fraction = progress)
            val endY = lerp(start = size.height * 0.8f, stop = size.height, fraction = progress)

            moveTo(0f, yStart)
            cubicTo(
                size.width * 0.3f, controlY1,
                size.width * 0.7f, controlY2,
                size.width, endY
            )
            lineTo(size.width, 0f)
            lineTo(0f, 0f)
            close()
        }

        // Segunda curva (detalle)
        val detailPath = Path().apply {
            moveTo(0f, size.height * (0.5f + (increment * 0.4f)))
            cubicTo(
                size.width * 0.2f,
                size.height * (0.2f + (increment * 0.4f)),
                size.width * 0.5f,
                size.height * (0.8f + (increment * 0.4f)),
                size.width,
                size.height * (0.4f + (increment * 0.4f))
            )
            lineTo(size.width, 0f)
            lineTo(0f, 0f)
            close()
        }

        // Tercera curva (accent)
        val accentPath = Path().apply {
            moveTo(0f, size.height * (0.3f + (increment * 0.2f)))
            cubicTo(
                size.width * 0.1f,
                size.height * (0.1f + (increment * 0.2f)),
                size.width * 0.3f,
                size.height * (0.4f + (increment * 0.2f)),
                size.width,
                size.height * (0.2f + (increment * 0.2f))
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
                end = Offset(size.width, size.height * (1.2f + increment))
            )
        )

        drawPath(
            path = detailPath,
            brush = Brush.linearGradient(
                colors = listOf(SecondaryPurple.copy(alpha = 0.7f), PrimaryBlueLight.copy(alpha = 0.7f)),
                start = Offset(0f, 0f),
                end = Offset(size.width, size.height * (0.8f + (increment * 0.2f)))
            )
        )

        drawPath(
            path = accentPath,
            brush = Brush.linearGradient(
                colors = listOf(PrimaryBlueLight.copy(alpha = 0.5f), PrimaryBlue.copy(alpha = 0.5f)),
                start = Offset(0f, 0f),
                end = Offset(size.width, size.height * (0.4f + (increment * 0.4f)))
            )
        )

        // Añadir algunos círculos decorativos
        drawCircle(
            color = PrimaryBlue.copy(alpha = 0.2f * progress),
            radius = size.width * (0.2f + (increment * 0.2f)),
            center = Offset(size.width * 0.2f, size.height * (0.3f ))
        )

        drawCircle(
            color = SecondaryPurple.copy(alpha = 0.2f * progress),
            radius = size.width * (0.15f + (increment * 0.2f)),
            center = Offset(size.width * 0.8f, size.height * (0.2f))
        )
    }
}