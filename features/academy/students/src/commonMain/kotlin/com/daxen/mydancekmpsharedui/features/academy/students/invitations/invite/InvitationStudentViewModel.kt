package com.daxen.mydancekmpsharedui.features.academy.students.invitations.invite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daxen.mydancekmpsharedui.data.students.model.Invitation
import com.daxen.mydancekmpsharedui.data.students.repository.AcademyStudentsRepository
import com.daxen.mydancekmpsharedui.data.user.model.UserAcademy
import com.daxen.mydancekmpsharedui.data.user.repository.AcademyUserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    
    private val _uiState = MutableStateFlow<InviteStudentUiState>(InviteStudentUiState.Loading)
    val uiState: StateFlow<InviteStudentUiState> = _uiState.asStateFlow()
    
    private val _invitations = MutableStateFlow<List<Invitation>>(emptyList())
    
    private val _isModalVisible = MutableStateFlow(false)
    val isModalVisible: StateFlow<Boolean> = _isModalVisible.asStateFlow()
    
    private val _studentEmail = MutableStateFlow("")
    val studentEmail: StateFlow<String> = _studentEmail.asStateFlow()
    
    private val _isSubmitting = MutableStateFlow(false)
    val isSubmitting: StateFlow<Boolean> = _isSubmitting.asStateFlow()
    
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    
    init {
        loadCurrentAcademy()
        simulateInvitations()
    }
    
    private fun loadCurrentAcademy() {
        viewModelScope.launch {
            try {
                academyUserRepository.updateCurrentAcademy()
            } catch (e: Exception) {
                _errorMessage.value = "Error al cargar la academia: ${e.message}"
            }
        }
    }
    
    private fun simulateInvitations() {
        viewModelScope.launch {
            // Simulamos carga
            _uiState.value = InviteStudentUiState.Loading
            
            // En una implementación real, aquí cargaríamos las invitaciones desde Firebase
            val dummyInvitations = listOf(
                Invitation(
                    email = "estudiante1@example.com",
                    academyId = "academyId1",
                    academyName = "Mi Academia",
                    status = "pending"
                ),
                Invitation(
                    email = "estudiante2@example.com",
                    academyId = "academyId1",
                    academyName = "Mi Academia",
                    status = "accepted"
                )
            )
            
            _invitations.value = dummyInvitations
            
            if (dummyInvitations.isEmpty()) {
                _uiState.value = InviteStudentUiState.Empty
            } else {
                _uiState.value = InviteStudentUiState.Success(dummyInvitations)
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
                val result = academyStudentsRepository.inviteStudentToAcademy(
                    academyId = currentAcademy.value.uid,
                    studentId = email, // Usamos el email como ID temporal
                    academyName = currentAcademy.value.name
                )
                
                if (result) {
                    // Simulamos agregar la invitación a la lista
                    val newInvitation = Invitation(
                        email = email,
                        academyId = currentAcademy.value.uid,
                        academyName = currentAcademy.value.name,
                        status = "pending"
                    )
                    
                    val updatedInvitations = _invitations.value.toMutableList().apply {
                        add(newInvitation)
                    }
                    
                    _invitations.value = updatedInvitations
                    _uiState.value = InviteStudentUiState.Success(updatedInvitations)
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
    
    private fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")
        return emailRegex.matches(email)
    }
}