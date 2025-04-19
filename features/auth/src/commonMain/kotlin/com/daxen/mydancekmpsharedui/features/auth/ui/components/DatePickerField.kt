package com.daxen.mydancekmpsharedui.features.auth.ui.components

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.datetime.*

@Composable
fun DatePickerField(
    onValueChange: (String) -> Unit,
    error: String? = null,
    modifier: Modifier = Modifier
) {
    var selectedDate by remember { mutableStateOf<Long?>(null) }
    var showModal by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = selectedDate?.let { convertMillisToDate(it) } ?: "",
        onValueChange = { onValueChange(it) },
        label = { Text("Fecha de nacimiento") },
        placeholder = { Text("MM/DD/YYYY") },
        isError = error != null,
        readOnly = true,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.CalendarToday,
                contentDescription = "Seleccionar fecha"
            )
        },
        supportingText = {
            if (error != null) {
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = if (error != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = if (error != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
            focusedTextColor = MaterialTheme.colorScheme.onBackground,
            unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
            focusedLabelColor = if (error != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
            unfocusedLabelColor = if (error != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
        ),
        modifier = modifier
            .fillMaxWidth()
            .pointerInput(selectedDate) {
                awaitEachGesture {
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) {
                        showModal = true
                    }
                }
            }
    )

    if (showModal) {
        DatePickerModal(
            onDateSelected = { selectedDate = it },
            onDismiss = { showModal = false }
        )
    }
}

fun convertMillisToDate(millis: Long): String {
    val instant = Instant.fromEpochMilliseconds(millis)
    val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    val date = dateTime.date

    val month = date.monthNumber.toString().padStart(2, '0')
    val day = date.dayOfMonth.toString().padStart(2, '0')
    val year = date.year.toString()

    return "$day-$month-$year"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        colors = DatePickerDefaults.colors(
            containerColor = MaterialTheme.colorScheme.onPrimary,
        ),
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("Seleccionar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    ) {
        DatePicker(
            colors = DatePickerDefaults.colors(
                containerColor = MaterialTheme.colorScheme.onPrimary,
            ),
            showModeToggle = false,
            state = datePickerState
        )
    }
}