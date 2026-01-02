package com.daxen.mydancekmpsharedui.features.academy.teachers.invitations.invite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daxen.mydancekmpsharedui.data.teachers.model.Invitation
import com.daxen.mydancekmpsharedui.data.teachers.model.InvitationStatus
import com.daxen.mydancekmpsharedui.data.teachers.repository.AcademyTeachersRepository
import com.daxen.mydancekmpsharedui.data.user.model.UserAcademy
import com.daxen.mydancekmpsharedui.data.user.repository.AcademyUserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant

sealed class InviteTeacherUiState {
    data object Loading : InviteTeacherUiState()
    data class Success(val invitations: List<Invitation>) : InviteTeacherUiState()
    data object Empty : InviteTeacherUiState()
    data class Error(val message: String) : InviteTeacherUiState()
}

class InvitationTeacherViewModel(
    private val academyTeachersRepository: AcademyTeachersRepository,
    academyUserRepository: AcademyUserRepository
) : ViewModel() {
    private val currentAcademy: StateFlow<UserAcademy> = academyUserRepository.currentAcademy
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    private val _isModalVisible = MutableStateFlow(false)
    val isModalVisible: StateFlow<Boolean> = _isModalVisible.asStateFlow()
    
    private val _teacherEmail = MutableStateFlow("")
    val teacherEmail: StateFlow<String> = _teacherEmail.asStateFlow()
    
    private val _isSubmitting = MutableStateFlow(false)
    val isSubmitting: StateFlow<Boolean> = _isSubmitting.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    
    private val _formErrorMessage = MutableStateFlow<String?>(null)
    val formErrorMessage: StateFlow<String?> = _formErrorMessage.asStateFlow()
    
    private val _isLoading = MutableStateFlow(true)
    
    private val _showDeleteDialog = MutableStateFlow(false)
    val showDeleteDialog: StateFlow<Boolean> = _showDeleteDialog.asStateFlow()
    
    private val _invitationToDelete = MutableStateFlow<Invitation?>(null)
    val invitationToDelete: StateFlow<Invitation?> = _invitationToDelete.asStateFlow()
    
    private val _isDeletingInvitation = MutableStateFlow(false)
    val isDeletingInvitation: StateFlow<Boolean> = _isDeletingInvitation.asStateFlow()
    
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()
    
    // Primero ordenamos las invitaciones por fecha de creación (más recientes primero)
    private val sortedInvitations = academyTeachersRepository.invitations.map { invitations ->
        invitations.sortedByDescending { it.createdAt }
    }
    
    // Luego aplicamos el filtro de búsqueda sobre las invitaciones ya ordenadas
    private val filteredInvitations = combine(
        sortedInvitations, _searchQuery
    ) { sortedList, query ->
        if (query.isBlank()) sortedList
        else {
            val lowerQuery = query.lowercase()
            sortedList.filter {
                it.email.contains(lowerQuery, ignoreCase = true)
            }
        }
    }

    val uiState: StateFlow<InviteTeacherUiState> = combine(
        filteredInvitations, _isLoading, _errorMessage
    ) { filteredList, isLoading, error ->
        when {
            isLoading -> InviteTeacherUiState.Loading
            error != null -> InviteTeacherUiState.Error(error)
            filteredList.isEmpty() -> InviteTeacherUiState.Empty
            else -> InviteTeacherUiState.Success(filteredList)
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        InviteTeacherUiState.Loading
    )
    
    init {
        loadCurrentAcademy()
    }
    
    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }
    
    private fun loadCurrentAcademy() {
        viewModelScope.launch {
            try {
                loadInvitations()
            } catch (e: Exception) {
                _errorMessage.value = "Error al cargar la academia: ${e.message}"
                _isLoading.value = false
            }
        }
    }
    
    private fun loadInvitations() {
        viewModelScope.launch {
            if (!_isRefreshing.value) {
                _isLoading.value = true
            }
            
            try {
                val academyId = currentAcademy.value.academyId
                if (academyId.isNotEmpty()) {
                    academyTeachersRepository.getTeachersByAcademyID(academyId)
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error al cargar invitaciones: ${e.message}"
            } finally {
                _isLoading.value = false
                _isRefreshing.value = false
            }
        }
    }
    
    fun toggleModal() {
        _isModalVisible.value = !_isModalVisible.value
        if (!_isModalVisible.value) {
            _teacherEmail.value = ""
            _formErrorMessage.value = null
        }
    }
    
    fun updateTeacherEmail(email: String) {
        _teacherEmail.value = email
        if (_formErrorMessage.value != null) {
            _formErrorMessage.value = null
        }
    }

    fun sendInvitation() {
        viewModelScope.launch {
            val email = _teacherEmail.value.trim()
            
            if (email.isEmpty()) {
                _formErrorMessage.value = "El correo no puede estar vacío"
                return@launch
            }
            
            if (!isValidEmail(email)) {
                _formErrorMessage.value = "Introduce un correo electrónico válido"
                return@launch
            }

            _isSubmitting.value = true
            
            try {
                // Verificar si se puede crear la invitación
                val (canCreate, errorMessage) = academyTeachersRepository.canCreateInvitation(email, currentAcademy.value.academyId)
                
                if (!canCreate) {
                    _formErrorMessage.value = errorMessage
                    _isSubmitting.value = false
                    return@launch
                }
                
                // Crear un objeto Invitation completo
                val invitation = Invitation(
                    id = "",
                    email = email,
                    academyId = currentAcademy.value.academyId,
                    academyName = currentAcademy.value.name,
                    status = InvitationStatus.PENDING,
                    createdAt = Instant.DISTANT_PAST
                )
                
                val result = academyTeachersRepository.inviteTeacherToAcademy(invitation)
                
                if (result) {
                    loadInvitations()
                    toggleModal()
                } else {
                    _formErrorMessage.value = "Error al enviar la invitación"
                }
            } catch (e: Exception) {
                _formErrorMessage.value = "Error: ${e.message}"
            } finally {
                _isSubmitting.value = false
            }
        }
    }
    
    fun showDeleteConfirmationDialog(invitation: Invitation) {
        _invitationToDelete.value = invitation
        _showDeleteDialog.value = true
    }
    
    fun hideDeleteConfirmationDialog() {
        _showDeleteDialog.value = false
        _invitationToDelete.value = null
    }
    
    fun deleteInvitation() {
        val invitation = _invitationToDelete.value ?: return
        
        // Verificar que la invitación tenga un ID
        if (invitation.id.isEmpty()) {
            _errorMessage.value = "No se puede eliminar: ID de invitación no válido"
            hideDeleteConfirmationDialog()
            return
        }
        
        viewModelScope.launch {
            _isDeletingInvitation.value = true
            
            try {
                val result = academyTeachersRepository.deleteInvitation(invitation.id)
                
                if (result) {
                    hideDeleteConfirmationDialog()
                } else {
                    _errorMessage.value = "Error al eliminar la invitación"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error al eliminar la invitación: ${e.message}"
            } finally {
                _isDeletingInvitation.value = false
            }
        }
    }
    
    fun refreshInvitations() {
        _isRefreshing.value = true
        _errorMessage.value = null
        viewModelScope.launch {
            try {
                val academyId = currentAcademy.value.academyId
                println(currentAcademy.value)
                if (academyId.isNotEmpty()) {
                    academyTeachersRepository.getTeachersByAcademyID(academyId)
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error al actualizar: ${e.message}"
            } finally {
                _isRefreshing.value = false
            }
        }
    }
    
    private fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")
        return emailRegex.matches(email)
    }
} 