package com.daxen.mydancekmpsharedui.features.user.ui.sections

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
internal fun ProfileInfo(
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
        // Tu contenido aquí
        Column(
            modifier = Modifier
            .padding(padding)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
        ) {
            Text("PROBANDO")
            Text("PROBANDO")
            Text("PROBANDO")
            Text("PROBANDO")
            Text("PROBANDO")
            Text("PROBANDO")
            Text("PROBANDO")
            Text("PROBANDO")
            Text("PROBANDO")
            Text("PROBANDO")
            Text("PROBANDO")
            Text("PROBANDO")
            Text("PROBANDO")
        }
    }
}