package com.daxen.mydancekmpsharedui.features.user.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.daxen.mydancekmpsharedui.features.user.ProfileAction
import com.daxen.mydancekmpsharedui.features.user.utils.ProfileItem

@Composable
internal fun ProfileSectionCard(
    title: String,
    options: List<ProfileItem.Option>,
    navigateToSection: (ProfileAction) -> Unit,
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 8.dp)
    )
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column {
            options.forEachIndexed { index, option ->
                ProfileOptionItem(
                    icon = option.icon,
                    label = option.label,
                    color = option.color
                ) {
                    option.onClick?.let { navigateToSection(it) }
                }

                if (index < options.lastIndex) {
                    Divider(modifier = Modifier.padding(start = 16.dp))
                }
            }
        }
    }
}