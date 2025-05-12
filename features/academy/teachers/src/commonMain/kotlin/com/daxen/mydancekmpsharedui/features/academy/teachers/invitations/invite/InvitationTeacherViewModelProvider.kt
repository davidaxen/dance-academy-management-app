package com.daxen.mydancekmpsharedui.features.academy.teachers.invitations.invite

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Provider para obtener la instancia compartida del ViewModel de invitaciones de profesores
 */
object InvitationTeacherViewModelProvider : KoinComponent {
    private val viewModel: InvitationTeacherViewModel by inject()
    
    fun get(): InvitationTeacherViewModel = viewModel
} 