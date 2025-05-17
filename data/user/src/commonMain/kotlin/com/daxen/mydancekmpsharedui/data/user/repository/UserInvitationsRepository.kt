package com.daxen.mydancekmpsharedui.data.user.repository

import com.daxen.mydancekmpsharedui.data.user.model.Academy
import com.daxen.mydancekmpsharedui.data.user.model.UserInvitation
import kotlinx.coroutines.flow.StateFlow

interface UserInvitationsRepository {
    val invitations: StateFlow<List<UserInvitation>>
    val academies: StateFlow<List<Academy>>
    
    suspend fun fetchUserInvitations(email: String)
    suspend fun fetchUserAcademies(email: String)
    suspend fun acceptInvitation(invitation: UserInvitation): Boolean
    suspend fun rejectInvitation(invitationId: String): Boolean
} 