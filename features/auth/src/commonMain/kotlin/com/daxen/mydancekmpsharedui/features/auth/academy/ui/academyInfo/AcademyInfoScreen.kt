package com.daxen.mydancekmpsharedui.features.auth.academy.ui.academyInfo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.daxen.mydancekmpsharedui.core.ui.LocalPadding
import com.daxen.mydancekmpsharedui.core.ui.theme.BackgroundLight
import com.daxen.mydancekmpsharedui.features.auth.ui.components.*

@Composable
fun AcademyInfoScreen(
    viewModel: AcademyInfoViewModel,
    onNavigateNext: () -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.academyInfoState.collectAsState()
    val name by viewModel.name.collectAsState()
    val nameError by viewModel.nameError.collectAsState()
    val nif by viewModel.nif.collectAsState()
    val nifError by viewModel.nifError.collectAsState()
    val address by viewModel.address.collectAsState()
    val addressError by viewModel.addressError.collectAsState()
    val openingTime by viewModel.openingTime.collectAsState()
    val openingTimeError by viewModel.openingTimeError.collectAsState()
    val closingTime by viewModel.closingTime.collectAsState()
    val closingTimeError by viewModel.closingTimeError.collectAsState()
    val isSubmitting by viewModel.isSubmitting.collectAsState()

    var isNavigating by remember { mutableStateOf(false) }

    LaunchedEffect(state) {
        if (state is AcademyInfoState.Success && !isNavigating) {
            isNavigating = true
            onNavigateNext()
            viewModel.onBackClicked()
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
                modifier = Modifier.fillMaxSize()
                        .padding(horizontal = LocalPadding.current.large),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AuthTitleAndSubtitle(
                    title = "Información de la Academia",
                    subtitle = "Completa la información de tu academia para seguir adelante",
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                AuthCard {
                    CustomTextField(
                        value = name,
                        onValueChange = { viewModel.updateName(it) },
                        label = "Nombre de la academia",
                        error = nameError,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next,
                        leadingIcon = Icons.Default.Business,
                        leadingIconDescription = "Nombre de la academia"
                    )

                    CustomTextField(
                        value = nif,
                        onValueChange = { viewModel.updateLegalName(it) },
                        label = "NIF",
                        error = nifError,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next,
                        leadingIcon = Icons.Default.Description,
                        leadingIconDescription = "Nombre legal"
                    )

                    CustomTextField(
                        value = address,
                        onValueChange = { viewModel.updateAddress(it) },
                        label = "Dirección",
                        error = addressError,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next,
                        leadingIcon = Icons.Default.LocationOn,
                        leadingIconDescription = "Dirección"
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TimePickerField(
                            value = openingTime,
                            onValueChange = { viewModel.updateOpeningTime(it) },
                            label = "Apertura",
                            error = openingTimeError,
                            modifier = Modifier.weight(1f)
                        )

                        TimePickerField(
                            value = closingTime,
                            onValueChange = { viewModel.updateClosingTime(it) },
                            label = "Cierre",
                            error = closingTimeError,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    when (state) {
                        is AcademyInfoState.Error -> {
                            Text(
                                text = (state as AcademyInfoState.Error).message,
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
                                isLoading = isSubmitting,
                            )
                        }
                    }
                }
            }
        }
    }
}