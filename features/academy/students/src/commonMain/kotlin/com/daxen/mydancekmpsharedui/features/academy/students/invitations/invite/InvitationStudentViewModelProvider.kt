package com.daxen.mydancekmpsharedui.features.academy.students.invitations.invite

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Provider para obtener la instancia compartida del ViewModel de invitaciones
 */
object InvitationStudentViewModelProvider : KoinComponent {
    private val viewModel: InvitationStudentViewModel by inject()
    
    fun get(): InvitationStudentViewModel = viewModel
} 