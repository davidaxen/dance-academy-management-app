package com.daxen.mydancekmpsharedui.features.user.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.daxen.mydancekmpsharedui.data.user.model.User
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.daxen.mydancekmpsharedui.core.ui.LocalPadding

@Composable
fun UserDataSummary(user: User, navigateToLogin: () -> Unit) {
    Surface(
        elevation = 4.dp,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(
                    vertical = LocalPadding.current.small,
                    horizontal = LocalPadding.current.small
                )
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(start = LocalPadding.current.tiny)
                    .fillMaxWidth(0.7f)
            ) {
                Text(
                    text = user.name,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = user.email,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.width(18.dp))

            IconButton(
                onClick = navigateToLogin,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = "Cerrar sesión",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}