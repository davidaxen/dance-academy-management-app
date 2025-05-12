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
    
    // Guardamos el email del usuario actual para operaciones posteriores
    private var currentUserEmail: String = ""
    
    override suspend fun fetchUserInvitations(email: String) {
        currentUserEmail = email
        val invitationModels = firebaseUserInvitationsService.getUserInvitations(email)
        _invitations.value = invitationModels.map { it.toUserInvitation() }
    }
    
    override suspend fun fetchUserAcademies(email: String) {
        currentUserEmail = email
        val academyIds = firebaseUserInvitationsService.getUserAcademies(email)
        
        if (academyIds.isEmpty()) {
            _academies.value = emptyList()
            return
        }
        
        // Obtenemos los detalles de cada academia
        val academyDetailsList = academyIds.map { academyId ->
            firebaseUserInvitationsService.getAcademyDetails(academyId, email)
        }
        
        // Convertimos los detalles a nuestro modelo
        _academies.value = academyDetailsList.map { details ->
            Academy(
                id = details["id"] as String,
                name = details["name"] as String,
                location = details["location"] as String,
                imageUrl = details["imageUrl"] as String,
                schedule = details["schedule"] as String,
                role = UserRole.fromString(details["role"] as String)
            )
        }
    }
    
    override suspend fun acceptInvitation(invitationId: String): Boolean {
        val success = firebaseUserInvitationsService.acceptInvitation(invitationId)
        
        if (success) {
            // Actualizamos la lista local
            _invitations.value = _invitations.value.filter { it.id != invitationId }
            
            // Si tenemos guardado el email del usuario, recargamos sus academias
            if (currentUserEmail.isNotEmpty()) {
                fetchUserAcademies(currentUserEmail)
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