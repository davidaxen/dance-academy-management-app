package com.daxen.mydancekmpsharedui.data.user.repository

import com.daxen.mydancekmpsharedui.core.firebase.user.FirebaseUserInvitationsService
import com.daxen.mydancekmpsharedui.data.user.model.Academy
import com.daxen.mydancekmpsharedui.data.user.model.UserInvitation
import com.daxen.mydancekmpsharedui.data.user.model.UserRole
import com.daxen.mydancekmpsharedui.data.user.model.mapper.toUserInvitation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserInvitationsRepositoryImpl(
    private val firebaseUserInvitationsService: FirebaseUserInvitationsService
) : UserInvitationsRepository {
    private val _invitations = MutableStateFlow<List<UserInvitation>>(emptyList())
    override val invitations: StateFlow<List<UserInvitation>> = _invitations.asStateFlow()
    
    private val _academies = MutableStateFlow<List<Academy>>(emptyList())
    override val academies: StateFlow<List<Academy>> = _academies.asStateFlow()
    
    override suspend fun fetchUserInvitations(email: String) {
        val invitationModels = firebaseUserInvitationsService.getUserInvitations(email)
        _invitations.value = invitationModels.map { it.toUserInvitation() }
    }
    
    override suspend fun fetchUserAcademies(email: String) {
        val academyIds = firebaseUserInvitationsService.getUserAcademies(email)
        
        // Aquí deberíamos cargar más información de cada academia
        // Por ahora usaremos datos básicos
        _academies.value = academyIds.map { academyId ->
            Academy(
                id = academyId,
                name = "Academia $academyId",
                location = "Ubicación pendiente",
                role = UserRole.STUDENT,
                schedule = "Horario pendiente"
            )
        }
    }
    
    override suspend fun acceptInvitation(invitationId: String): Boolean {
        val success = firebaseUserInvitationsService.acceptInvitation(invitationId)
        
        if (success) {
            // Actualizamos la lista local
            _invitations.value = _invitations.value.filter { it.id != invitationId }
            
            // Deberíamos recargar las academias también
            val currentUser = _invitations.value.firstOrNull()?.let {
                // Asumimos que todas las invitaciones son para el mismo usuario
                firebaseUserInvitationsService.getUserAcademies(it.id)
            }
            
            // Si tenemos el email del usuario, recargamos sus academias
            if (currentUser != null) {
                fetchUserAcademies(currentUser.toString())
            }
        }
        
        return success
    }
    
    override suspend fun rejectInvitation(invitationId: String): Boolean {
        val success = firebaseUserInvitationsService.rejectInvitation(invitationId)
        
        if (success) {
            // Actualizamos la lista local
            _invitations.value = _invitations.value.filter { it.id != invitationId }
        }
        
        return success
    }
} 