package com.daxen.mydancekmpsharedui.features.academy.students.invitations.invite

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.PendingActions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.dropUnlessResumed
import com.daxen.mydancekmpsharedui.core.ui.LocalPadding
import com.daxen.mydancekmpsharedui.core.ui.composables.ErrorComponent
import com.daxen.mydancekmpsharedui.core.ui.composables.LoadingComponent
import com.daxen.mydancekmpsharedui.data.students.model.Invitation
import com.daxen.mydancekmpsharedui.data.students.model.InvitationStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvitationStudentScreen(
    viewModel: InvitationStudentViewModel,
) {
    val uiState by viewModel.uiState.collectAsState()
    val isModalVisible by viewModel.isModalVisible.collectAsState()
    val showDeleteDialog by viewModel.showDeleteDialog.collectAsState()
    val invitationToDelete by viewModel.invitationToDelete.collectAsState()
    val isDeletingInvitation by viewModel.isDeletingInvitation.collectAsState()
    val sheetState = rememberModalBottomSheetState()
    
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = dropUnlessResumed { viewModel.toggleModal() },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Invitar estudiante"
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = uiState) {
                is InviteStudentUiState.Loading -> {
                    LoadingComponent(text = "Cargando invitaciones...")
                }
                is InviteStudentUiState.Success -> {
                    InvitationsList(
                        invitations = state.invitations,
                        onDeleteClick = { invitation ->
                            viewModel.showDeleteConfirmationDialog(invitation)
                        }
                    )
                }
                is InviteStudentUiState.Empty -> {
                    EmptyContent()
                }
                is InviteStudentUiState.Error -> {
                    ErrorComponent(
                        message = state.message,
                        onRetry = { viewModel.refreshInvitations() }
                    )
                }
            }
        }
    }
    
    if (isModalVisible) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.toggleModal() },
            sheetState = sheetState
        ) {
            InviteStudentForm(viewModel)
        }
    }
    
    // Diálogo de confirmación para eliminar invitación
    if (showDeleteDialog && invitationToDelete != null) {
        AlertDialog(
            onDismissRequest = { viewModel.hideDeleteConfirmationDialog() },
            title = { Text("Eliminar invitación") },
            text = { 
                Text("¿Estás seguro que deseas eliminar la invitación enviada a ${invitationToDelete?.email}?") 
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.deleteInvitation() },
                    enabled = !isDeletingInvitation
                ) {
                    if (isDeletingInvitation) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { viewModel.hideDeleteConfirmationDialog() },
                    enabled = !isDeletingInvitation
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun InvitationsList(
    invitations: List<Invitation>,
    onDeleteClick: (Invitation) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        itemsIndexed(invitations) { index, invitation ->
            InvitationListItem(
                invitation = invitation,
                onDeleteClick = onDeleteClick
            )
            
            if (index < invitations.size - 1) {
                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f)
                )
            }
        }
    }
}

@Composable
private fun InvitationListItem(
    invitation: Invitation,
    onDeleteClick: (Invitation) -> Unit
) {
    // Determinar el icono y color basado en el estado
    val (icon, color, statusText) = when (invitation.status) {
        InvitationStatus.PENDING -> Triple(
            Icons.Default.PendingActions,
            Color(0xFFFFA000),
            InvitationStatus.PENDING.localizedValue
        )
        InvitationStatus.ACCEPTED -> Triple(
            Icons.Default.CheckCircle,
            Color(0xFF4CAF50),
            InvitationStatus.ACCEPTED.localizedValue
        )
        else -> Triple(
            Icons.Default.ErrorOutline,
            Color(0xFFF44336),
            InvitationStatus.REJECTED.localizedValue
        )
    }
    
    val showDeleteButton = invitation.status == InvitationStatus.PENDING
    
    ListItem(
        leadingContent = {
            StatusIcon(icon = icon, color = color)
        },
        headlineContent = {
            Text(
                text = invitation.email,
                style = MaterialTheme.typography.titleMedium
            )
        },
        supportingContent = {
            Text(
                text = "Estado: $statusText",
                style = MaterialTheme.typography.bodyMedium,
                color = color
            )
        },
        trailingContent = {
            if (showDeleteButton) {
                IconButton(onClick = { onDeleteClick(invitation) }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar invitación",
                        tint = Color(0xFFF44336)
                    )
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = LocalPadding.current.extraTiny)
    )
}

@Composable
private fun StatusIcon(icon: ImageVector, color: Color) {
    Surface(
        color = color.copy(alpha = 0.1f),
        shape = CircleShape,
        modifier = Modifier.size(40.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = color
            )
        }
    }
}

@Composable
private fun EmptyContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Email,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "No hay invitaciones",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Pulsa el botón + para invitar a estudiantes a tu academia",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )
    }
}

@Composable
private fun InviteStudentForm(viewModel: InvitationStudentViewModel) {
    val studentEmail by viewModel.studentEmail.collectAsState()
    val isSubmitting by viewModel.isSubmitting.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = LocalPadding.current.normal, vertical = LocalPadding.current.normal),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Invitar Estudiante",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(LocalPadding.current.normal))
        
        OutlinedTextField(
            value = studentEmail,
            onValueChange = { viewModel.updateStudentEmail(it) },
            label = { Text("Correo electrónico") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = null
                )
            },
            isError = errorMessage != null,
            supportingText = errorMessage?.let { { Text(it) } },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isSubmitting
        )
        
        Spacer(modifier = Modifier.height(LocalPadding.current.normal))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(
                onClick = { viewModel.toggleModal() },
                enabled = !isSubmitting
            ) {
                Text("Cancelar")
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Button(
                onClick = { viewModel.sendInvitation() },
                enabled = !isSubmitting
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text("Enviar invitación")
            }
        }
        
        Spacer(modifier = Modifier.height(LocalPadding.current.small))
    }
}