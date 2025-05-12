package com.daxen.mydancekmpsharedui.features.user.ui.academyUser.sections.academyData

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun AcademyDataScreen(
    viewModel: AcademyDataViewModel = koinViewModel(),
    navigateBack: () -> Unit
) {
    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text("Datos de la academia", color = MaterialTheme.colorScheme.onPrimary) },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, tint = MaterialTheme.colorScheme.onPrimary, contentDescription = "Atrás")
                    }
                },
                backgroundColor = MaterialTheme.colorScheme.primary,
            )
        }
    ) { padding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 8.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = viewModel.name,
                onValueChange = { viewModel.updateName(it)},
                label = { Text("Nombre") },
                modifier = Modifier.padding(8.dp).fillMaxWidth()
            )
            OutlinedTextField(
                value = viewModel.email,
                onValueChange = { viewModel.updateEmail(it)},
                label = { Text("Correo electrónico") },
                modifier = Modifier.padding(8.dp).fillMaxWidth()
            )
            OutlinedTextField(
                value = viewModel.nif,
                onValueChange = { viewModel.updateNif(it)},
                label = { Text("NIF/CIF") },
                modifier = Modifier.padding(8.dp).fillMaxWidth()
            )
            OutlinedTextField(
                value = viewModel.address,
                onValueChange = { viewModel.updateAddress(it)},
                label = { Text("Dirección") },
                modifier = Modifier.padding(8.dp).fillMaxWidth()
            )
            OutlinedTextField(
                value = viewModel.openingTime,
                onValueChange = { viewModel.updateOpeningTime(it)},
                label = { Text("Hora de apertura") },
                modifier = Modifier.padding(8.dp).fillMaxWidth()
            )
            OutlinedTextField(
                value = viewModel.closingTime,
                onValueChange = { viewModel.updateClosingTime(it)},
                label = { Text("Hora de cierre") },
                modifier = Modifier.padding(8.dp).fillMaxWidth()
            )
        }
    }
} 