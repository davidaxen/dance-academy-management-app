package com.daxen.mydancekmpsharedui.features.auth.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.daxen.mydancekmpsharedui.core.ui.theme.PrimaryBlue

@Composable
fun LoginButton(
    isLoading: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = PrimaryBlue,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        enabled = !isLoading
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                strokeWidth = 2.dp
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(
            text = if (isLoading) "Iniciando sesión..." else "Iniciar sesión",
            style = MaterialTheme.typography.bodyLarge
        )
    }
} 