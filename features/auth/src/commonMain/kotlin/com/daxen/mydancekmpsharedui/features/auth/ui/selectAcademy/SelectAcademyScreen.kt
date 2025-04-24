package com.daxen.mydancekmpsharedui.features.auth.ui.selectAcademy

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.daxen.mydancekmpsharedui.core.ui.LocalPadding
import com.daxen.mydancekmpsharedui.core.ui.theme.BackgroundLight
import com.daxen.mydancekmpsharedui.features.auth.ui.components.AcademySearchField
import com.daxen.mydancekmpsharedui.features.auth.ui.components.AuthCard
import com.daxen.mydancekmpsharedui.features.auth.ui.components.AuthTitleAndSubtitle
import com.daxen.mydancekmpsharedui.features.auth.ui.components.CurvedBackgroundFull
import com.daxen.mydancekmpsharedui.features.auth.ui.components.CustomTextField
import com.daxen.mydancekmpsharedui.features.auth.ui.components.TopBarBackSection

@Composable
fun SelectAcademyScreen(
    viewModel: SelectAcademyViewModel
) {
    val selectedOption by viewModel.selectedOption.collectAsState()
    val academyCode by viewModel.academyCode.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val academies by viewModel.academies.collectAsState()

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
                        // Opción de código
                        RadioButtonOption(
                            text = "Usar código de academia",
                            selected = selectedOption == SelectAcademyOption.CODE,
                            onClick = { viewModel.updateSelectedOption(SelectAcademyOption.CODE) }
                        )

                        if (selectedOption == SelectAcademyOption.CODE) {
                            Spacer(modifier = Modifier.height(16.dp))
                            CustomTextField(
                                value = academyCode,
                                onValueChange = { viewModel.updateAcademyCode(it) },
                                label = "Código de academia",
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Opción de búsqueda
                        RadioButtonOption(
                            text = "Buscar academia",
                            selected = selectedOption == SelectAcademyOption.SEARCH,
                            onClick = { viewModel.updateSelectedOption(SelectAcademyOption.SEARCH) }
                        )

                        if (selectedOption == SelectAcademyOption.SEARCH) {
                            Spacer(modifier = Modifier.height(16.dp))
                            AcademySearchField(
                                searchQuery = searchQuery,
                                onSearchQueryChange = { viewModel.updateSearchQuery(it) },
                                academies = academies,
                                onAcademySelected = { /* TODO: Implementar selección de academia */ }
                            )
                        }
                    }
                }
            }

    }

}

@Composable
private fun RadioButtonOption(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    androidx.compose.foundation.layout.Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}