package com.daxen.mydancekmpsharedui.features.user.ui.sections.personalData

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun PersonalDataScreen(
    viewModel: PersonalDataViewModel = koinViewModel(),
    navigateBack: () -> Unit
) {
    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text("Datos personales") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                },
                backgroundColor = Color.White,
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
        }
    }
}