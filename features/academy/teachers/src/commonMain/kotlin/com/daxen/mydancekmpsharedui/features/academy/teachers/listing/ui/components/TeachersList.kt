package com.daxen.mydancekmpsharedui.features.academy.teachers.listing.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.dropUnlessResumed
import com.daxen.mydancekmpsharedui.core.ui.LocalPadding
import com.daxen.mydancekmpsharedui.data.teachers.model.Teacher

@Composable
fun TeachersList(
    teachers: List<Teacher>,
    onTeacherClick: (
        teacher: Teacher
    ) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        itemsIndexed(teachers) { index, teacher ->
            TeacherListItem(teacher = teacher, onClick = dropUnlessResumed {
                onTeacherClick(teacher)
            })

            if (index < teachers.size - 1) {
                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@Composable
private fun TeacherListItem(
    teacher: Teacher,
    onClick: () -> Unit
) {
    ListItem(
        leadingContent = {
            TeacherAvatar(teacher = teacher)
        },
        headlineContent = {
            Text(
                text = "${teacher.name} ${teacher.lastName}",
                style = MaterialTheme.typography.titleMedium
            )
        },
        supportingContent = {
            Text(
                text = teacher.email,
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
            .clickable { onClick() }
            .padding(horizontal = LocalPadding.current.extraTiny)
    )
} 