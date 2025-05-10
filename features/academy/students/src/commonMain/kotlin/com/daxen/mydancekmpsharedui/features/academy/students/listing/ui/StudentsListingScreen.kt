package com.daxen.mydancekmpsharedui.features.academy.students.listing.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.daxen.mydancekmpsharedui.core.ui.LocalPadding

@Composable
fun StudentsListingScreen(
    viewModel: StudentsListingViewModel
) {
    val students by viewModel.students.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        itemsIndexed(students) { index, student ->
            StudentListItem(student = student)

            if (index < students.size - 1) {
                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@Composable
private fun StudentListItem(
    student: StudentModel
) {
    ListItem(
        headlineContent = {
            Text(
                text = "${student.name} ${student.surnames}",
                style = MaterialTheme.typography.titleMedium
            )
        },
        supportingContent = {
            Text(
                text = student.email,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        trailingContent = {
            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .clickable {

            }
            .padding(horizontal = LocalPadding.current.tiny)
            .padding(vertical = LocalPadding.current.extraTiny)
    )
}