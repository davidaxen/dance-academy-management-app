package com.daxen.mydancekmpsharedui.features.user.ui.academySelection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daxen.mydancekmpsharedui.data.auth.repository.AuthRepository
import com.daxen.mydancekmpsharedui.data.user.model.Academy
import com.daxen.mydancekmpsharedui.data.user.model.User
import com.daxen.mydancekmpsharedui.data.user.model.UserInvitation
import com.daxen.mydancekmpsharedui.data.user.repository.UserInvitationsRepository
import com.daxen.mydancekmpsharedui.data.user.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AcademySelectionViewModel(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
    private val userInvitationsRepository: UserInvitationsRepository,
): ViewModel() {
    val currentUser: StateFlow<User> = userRepository.currentUser

    private val _selectedTab = MutableStateFlow(0)
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    val academies: StateFlow<List<Academy>> = userInvitationsRepository.academies
    val invitations: StateFlow<List<UserInvitation>> = userInvitationsRepository.invitations

    private val _loadingInvitations = MutableStateFlow<Set<String>>(emptySet())
    val loadingInvitations: StateFlow<Set<String>> = _loadingInvitations.asStateFlow()
    
    private val _isRefreshingAcademies = MutableStateFlow(false)
    val isRefreshingAcademies: StateFlow<Boolean> = _isRefreshingAcademies.asStateFlow()
    
    private val _isRefreshingInvitations = MutableStateFlow(false)
    val isRefreshingInvitations: StateFlow<Boolean> = _isRefreshingInvitations.asStateFlow()
    
    private val _isInitialLoading = MutableStateFlow(true)
    val isInitialLoading: StateFlow<Boolean> = _isInitialLoading.asStateFlow()

    init {
        viewModelScope.launch {
            currentUser.collectLatest { user ->
                if (user.email.isNotEmpty()) {
                    loadUserData(user.email)
                    _isInitialLoading.value = false
                }
            }
        }
    }

    private suspend fun loadUserData(email: String) {
        try {
            userInvitationsRepository.fetchUserInvitations(email)
            userInvitationsRepository.fetchUserAcademies(email)
        } catch (e: Exception) {
            println("Error cargando datos del usuario: ${e.message}")
            // Aquí podríamos manejar el error, mostrar un mensaje, etc.
        }
    }

    fun onTabSelected(index: Int) {
        _selectedTab.value = index
        
        // Si cambiamos a una pestaña y no hay datos, intentamos cargarlos
        if (index == 0 && academies.value.isEmpty()) {
            refreshAcademies()
        } else if (index == 1 && invitations.value.isEmpty()) {
            refreshInvitations()
        }
    }
    
    fun refreshAcademies() {
        val userEmail = currentUser.value.email
        if (userEmail.isEmpty()) return
        
        viewModelScope.launch {
            try {
                _isRefreshingAcademies.value = true
                userInvitationsRepository.fetchUserAcademies(userEmail)
            } catch (e: Exception) {
                println("Error refrescando academias: ${e.message}")
            } finally {
                _isRefreshingAcademies.value = false
            }
        }
    }
    
    fun refreshInvitations() {
        val userEmail = currentUser.value.email
        if (userEmail.isEmpty()) return
        
        viewModelScope.launch {
            try {
                _isRefreshingInvitations.value = true
                userInvitationsRepository.fetchUserInvitations(userEmail)
            } catch (e: Exception) {
                println("Error refrescando invitaciones: ${e.message}")
            } finally {
                _isRefreshingInvitations.value = false
            }
        }
    }

    fun acceptInvitation(invitation: UserInvitation) {
        _loadingInvitations.value += invitation.id
        viewModelScope.launch {
            try {
                val success = userInvitationsRepository
                    .acceptInvitation(invitation)
                if (success) {
                    userInvitationsRepository.fetchUserAcademies(currentUser.value.email)
                }
            } catch (e: Exception) {
                println("Error aceptando invitación: ${e.message}")
            } finally {
                _loadingInvitations.value -= invitation.id
            }
        }
    }

    fun rejectInvitation(invitation: UserInvitation) {
        _loadingInvitations.value += invitation.id
        viewModelScope.launch {
            try {
                userInvitationsRepository.rejectInvitation(invitation.id)
            } catch (e: Exception) {
                println("Error rechazando invitación: ${e.message}")
            } finally {
                _loadingInvitations.value -= invitation.id
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            try {
                authRepository.logout()
                userRepository.logOut()
            }catch (e: Exception) {
                println("UserViewModel Error en logOut $e")
            }
        }
    }

    fun onAcademySelected(id: String) {
        userRepository.setUserAcademyId(id)
    }
}