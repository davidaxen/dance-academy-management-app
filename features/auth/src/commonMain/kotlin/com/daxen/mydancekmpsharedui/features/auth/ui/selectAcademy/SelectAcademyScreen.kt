package com.daxen.mydancekmpsharedui.features.auth.ui.selectAcademy

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.daxen.mydancekmpsharedui.core.ui.LocalPadding
import com.daxen.mydancekmpsharedui.core.ui.theme.BackgroundLight
import com.daxen.mydancekmpsharedui.features.auth.ui.components.AcademySearchField
import com.daxen.mydancekmpsharedui.features.auth.ui.components.AuthButton
import com.daxen.mydancekmpsharedui.features.auth.ui.components.AuthCard
import com.daxen.mydancekmpsharedui.features.auth.ui.components.AuthTitleAndSubtitle
import com.daxen.mydancekmpsharedui.features.auth.ui.components.CurvedBackgroundFull
import com.daxen.mydancekmpsharedui.features.auth.ui.components.CustomTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectAcademyScreen(
    viewModel: SelectAcademyViewModel
) {
    val selectedOption by viewModel.selectedOption.collectAsState()
    val academyCode by viewModel.academyCode.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val academies by viewModel.academies.collectAsState()
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        CurvedBackgroundFull()
        Column(modifier = Modifier.fillMaxSize()) {
//            TopBarBackSection(onNavigateBack)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = LocalPadding.current.large),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AuthTitleAndSubtitle(
                    title = "Seleccionar Academia",
                    subtitle = "En que academia quieres registrarte",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                AuthCard {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = it }
                        ) {
                            OutlinedTextField(
                                value = when (selectedOption) {
                                    SelectAcademyOption.CODE -> "Usar código de academia"
                                    SelectAcademyOption.SEARCH -> "Buscar por academia"
                                },
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                                    unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(MenuAnchorType.PrimaryNotEditable, true),
                            )

                            ExposedDropdownMenu(
                                expanded = expanded,
                                containerColor = MaterialTheme.colorScheme.surface,
                                onDismissRequest = { expanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Usar código de academia") },
                                    onClick = {
                                        viewModel.updateSelectedOption(SelectAcademyOption.CODE)
                                        expanded = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Buscar academia") },
                                    onClick = {
                                        viewModel.updateSelectedOption(SelectAcademyOption.SEARCH)
                                        expanded = false
                                    }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        when (selectedOption) {
                            SelectAcademyOption.CODE -> {
                                CustomTextField(
                                    value = academyCode,
                                    onValueChange = { viewModel.updateAcademyCode(it) },
                                    label = "Código de academia",
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                            SelectAcademyOption.SEARCH -> {
                                AcademySearchField(
                                    searchQuery = searchQuery,
                                    onSearchQueryChange = { viewModel.updateSearchQuery(it) },
                                    academies = academies,
                                    onAcademySelected = { /* TODO: Implementar selección de academia */ }
                                )
                            }
                        }

                        AuthButton(
                            text = "Continuar",
                            onClick = { /* TODO: Implementar acción de continuar */ },
                            isLoading = false,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}