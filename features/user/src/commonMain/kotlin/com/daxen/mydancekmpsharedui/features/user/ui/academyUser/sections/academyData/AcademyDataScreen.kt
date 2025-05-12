package com.daxen.mydancekmpsharedui.features.user.ui.academyUser.sections.academyData

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.daxen.mydancekmpsharedui.core.ui.LocalPadding
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun AcademyDataScreen(
    viewModel: AcademyDataViewModel = koinViewModel(),
    navigateBack: () -> Unit
) {
//    val scope = rememberCoroutineScope()
    
//    val imagePicker = rememberImagePickerLauncher(
//        selectionMode = SelectionMode.Single,
//        scope = scope,
//        onResult = { byteArrays ->
//            byteArrays.firstOrNull()?.let { bytes ->
//                viewModel.updateSelectedImage(bytes)
//            }
//        }
//    )
    
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
            // Sección de imagen de perfil
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(120.dp)
                    .padding(8.dp)
            ) {
                if (viewModel.selectedImage != null) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.TopEnd
                    ) {
                        AsyncImage(
                            model = viewModel.selectedImage,
                            contentDescription = "Logo seleccionado",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                        )
                        IconButton(
                            onClick = { viewModel.removeSelectedImage() },
                            modifier = Modifier.padding(LocalPadding.current.tiny)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Eliminar imagen",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                } else if (viewModel.logoUrl.isNotEmpty()) {
                    AsyncImage(
                        model = viewModel.logoUrl,
                        contentDescription = "Logo de la academia",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                    )
                }
//                else {
//                    IconButton(
//                        onClick = { imagePicker.launch() },
//                        modifier = Modifier.size(120.dp)
//                    ) {
//                        Icon(
//                            imageVector = Icons.Default.AccountCircle,
//                            contentDescription = "Subir logo",
//                            tint = MaterialTheme.colorScheme.primary,
//                            modifier = Modifier.size(120.dp)
//                        )
//                    }
//                }
            }
            
//            if (viewModel.logoUrl.isNotEmpty() && viewModel.selectedImage == null) {
//                Button(
//                    onClick = { imagePicker.launch() },
//                    colors = ButtonDefaults.buttonColors(
//                        backgroundColor = MaterialTheme.colorScheme.primary,
//                        contentColor = MaterialTheme.colorScheme.onPrimary
//                    )
//                ) {
//                    Text("Cambiar logo")
//                }
//            }

            Spacer(modifier = Modifier.height(16.dp))
            
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
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = { 
                    viewModel.saveChanges()
                    navigateBack()
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
            ) {
                Text("Guardar cambios")
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
} 