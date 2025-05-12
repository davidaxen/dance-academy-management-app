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

    init {
        viewModelScope.launch {
            currentUser.collectLatest { user ->
                if (user.email.isNotEmpty()) {
                    loadUserData(user.email)
                }
            }
        }
    }

    private suspend fun loadUserData(email: String) {
        userInvitationsRepository.fetchUserInvitations(email)
        userInvitationsRepository.fetchUserAcademies(email)
    }

    fun onTabSelected(index: Int) {
        _selectedTab.value = index
    }

    fun acceptInvitation(invitation: UserInvitation) {
        _loadingInvitations.value += invitation.id
        viewModelScope.launch {
            val success = userInvitationsRepository.acceptInvitation(invitation.id)
            
            if (success) {
                val user = currentUser.value
                if (user.email.isNotEmpty()) {
                    userInvitationsRepository.fetchUserAcademies(user.email)
                }
            }
            
            _loadingInvitations.value -= invitation.id
        }
    }

    fun rejectInvitation(invitation: UserInvitation) {
        _loadingInvitations.value += invitation.id
        viewModelScope.launch {
            userInvitationsRepository.rejectInvitation(invitation.id)
            _loadingInvitations.value -= invitation.id
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
}