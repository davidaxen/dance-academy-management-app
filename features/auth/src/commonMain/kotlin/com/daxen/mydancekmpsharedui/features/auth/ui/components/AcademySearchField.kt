package com.daxen.mydancekmpsharedui.features.auth.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.daxen.mydancekmpsharedui.features.auth.ui.selectAcademy.AcademyItem

@Composable
fun AcademySearchField(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    academies: List<AcademyItem>,
    onAcademySelected: (AcademyItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        CustomTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            label = "Buscar academia",
            modifier = Modifier.fillMaxWidth()
        )

        if (academies.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                items(academies) { academy ->
                    AcademySearchItem(
                        academy = academy,
                        onClick = { onAcademySelected(academy) }
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
private fun AcademySearchItem(
    academy: AcademyItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = academy.name,
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = academy.location,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
} 