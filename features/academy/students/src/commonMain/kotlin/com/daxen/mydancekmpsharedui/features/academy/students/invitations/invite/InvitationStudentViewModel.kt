package com.daxen.mydancekmpsharedui.features.academy.students.invitations.invite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daxen.mydancekmpsharedui.data.students.model.Invitation
import com.daxen.mydancekmpsharedui.data.students.model.InvitationStatus
import com.daxen.mydancekmpsharedui.data.students.repository.AcademyStudentsRepository
import com.daxen.mydancekmpsharedui.data.user.model.UserAcademy
import com.daxen.mydancekmpsharedui.data.user.repository.AcademyUserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class InviteStudentUiState {
    data object Loading : InviteStudentUiState()
    data class Success(val invitations: List<Invitation>) : InviteStudentUiState()
    data object Empty : InviteStudentUiState()
    data class Error(val message: String) : InviteStudentUiState()
}

class InvitationStudentViewModel(
    private val academyStudentsRepository: AcademyStudentsRepository,
    private val academyUserRepository: AcademyUserRepository
) : ViewModel() {
    private val currentAcademy: StateFlow<UserAcademy> = academyUserRepository.currentAcademy
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    private val _isModalVisible = MutableStateFlow(false)
    val isModalVisible: StateFlow<Boolean> = _isModalVisible.asStateFlow()
    
    private val _studentEmail = MutableStateFlow("")
    val studentEmail: StateFlow<String> = _studentEmail.asStateFlow()
    
    private val _isSubmitting = MutableStateFlow(false)
    val isSubmitting: StateFlow<Boolean> = _isSubmitting.asStateFlow()
    
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    
    private val _isLoading = MutableStateFlow(true)
    
    // Combinar las invitaciones con la consulta de búsqueda
    private val filteredInvitations = combine(
        academyStudentsRepository.invitations, _searchQuery
    ) { invitations, query ->
        if (query.isBlank()) invitations
        else {
            val lowerQuery = query.lowercase()
            invitations.filter {
                it.email.contains(lowerQuery, ignoreCase = true)
            }
        }
    }
    
    // Estado UI combinado
    val uiState: StateFlow<InviteStudentUiState> = combine(
        filteredInvitations, _isLoading, _errorMessage
    ) { filteredList, isLoading, error ->
        when {
            isLoading -> InviteStudentUiState.Loading
            error != null -> InviteStudentUiState.Error(error)
            filteredList.isEmpty() -> InviteStudentUiState.Empty
            else -> InviteStudentUiState.Success(filteredList)
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        InviteStudentUiState.Loading
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
                academyUserRepository.updateCurrentAcademy()
                // Una vez obtenemos la academia actual, cargamos las invitaciones
                loadInvitations()
            } catch (e: Exception) {
                _errorMessage.value = "Error al cargar la academia: ${e.message}"
                _isLoading.value = false
            }
        }
    }
    
    private fun loadInvitations() {
        viewModelScope.launch {
            _isLoading.value = true
            
            try {
                val academyId = currentAcademy.value.uid
                if (academyId.isNotEmpty()) {
                    academyStudentsRepository.getInvitationsByAcademyID(academyId)
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error al cargar invitaciones: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun toggleModal() {
        _isModalVisible.value = !_isModalVisible.value
        if (!_isModalVisible.value) {
            _studentEmail.value = ""
            _errorMessage.value = null
        }
    }
    
    fun updateStudentEmail(email: String) {
        _studentEmail.value = email
    }

    fun sendInvitation() {
        viewModelScope.launch {
            val email = _studentEmail.value.trim()
            
            if (email.isEmpty()) {
                _errorMessage.value = "El correo no puede estar vacío"
                return@launch
            }
            
            if (!isValidEmail(email)) {
                _errorMessage.value = "Introduce un correo electrónico válido"
                return@launch
            }

            _isSubmitting.value = true
            
            try {
                // Crear un objeto Invitation completo
                val invitation = Invitation(
                    email = email,
                    academyId = currentAcademy.value.uid,
                    academyName = currentAcademy.value.name,
                    status = InvitationStatus.PENDING
                )
                
                val result = academyStudentsRepository.inviteStudentToAcademy(invitation)
                
                if (result) {
                    loadInvitations()
                    toggleModal()
                } else {
                    _errorMessage.value = "Error al enviar la invitación"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.message}"
            } finally {
                _isSubmitting.value = false
            }
        }
    }
    
    fun refreshInvitations() {
        loadInvitations()
    }
    
    private fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")
        return emailRegex.matches(email)
    }
}