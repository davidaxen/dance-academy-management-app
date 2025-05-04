package com.daxen.mydancekmpsharedui.features.auth.academy.ui.logoUploader

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.daxen.mydancekmpsharedui.core.ui.LocalPadding
import com.daxen.mydancekmpsharedui.core.ui.theme.BackgroundLight
import com.daxen.mydancekmpsharedui.features.auth.ui.components.*
import com.preat.peekaboo.image.picker.SelectionMode
import com.preat.peekaboo.image.picker.rememberImagePickerLauncher

@Composable
fun LogoUploaderScreen(
    viewModel: LogoUploaderViewModel,
    onNavigateNext: () -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.logoUploaderState.collectAsState()
    val selectedImage by viewModel.selectedImage.collectAsState()
    val isSubmitting by viewModel.isSubmitting.collectAsState()

    var isNavigating by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    val imagePicker = rememberImagePickerLauncher(
        selectionMode = SelectionMode.Single,
        scope = scope,
        onResult = { byteArrays ->
            byteArrays.firstOrNull()?.let { bytes ->
                viewModel.updateSelectedImage(bytes)
            }
        }
    )

    LaunchedEffect(state) {
        if (state is LogoUploaderState.Success && !isNavigating) {
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
                    title = "Logo de la Academia",
                    subtitle = "Sube el logo de tu academia para personalizar tu perfil",
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                AuthCard {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .border(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .clip(RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable {
                                    imagePicker.launch()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            if (selectedImage != null) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.TopEnd
                                ) {
                                    AsyncImage(
                                        model = selectedImage,
                                        contentDescription = "Logo seleccionado",
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.FillBounds
                                    )
                                    IconButton(
                                        onClick = { viewModel.removeSelectedImage() },
                                        modifier = Modifier.padding(8.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Eliminar imagen",
                                            tint = MaterialTheme.colorScheme.error
                                        )
                                    }
                                }
                            } else {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Añadir logo",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(48.dp)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Toca para seleccionar una imagen",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    when (state) {
                        is LogoUploaderState.Error -> {
                            Text(
                                text = (state as LogoUploaderState.Error).message,
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