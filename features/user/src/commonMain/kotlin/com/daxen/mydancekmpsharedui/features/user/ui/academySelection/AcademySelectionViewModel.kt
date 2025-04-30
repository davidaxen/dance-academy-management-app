package com.daxen.mydancekmpsharedui.features.user.ui.academySelection

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class Academy(
    val name: String,
    val location: String,
    val imageUrl: String,
    val rating: Float,
    val classCount: Int
)

data class Invitation(
    val academyName: String,
    val academyImage: String,
    val invitationDate: String
)

class AcademySelectionViewModel: ViewModel() {
    private val _selectedTab = MutableStateFlow(0)
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    private val _academies = MutableStateFlow(listOf(
        Academy(
            name = "Academia de Salsa",
            location = "Calle Mayor 123, Madrid",
            imageUrl = "https://rickandmortyapi.com/api/character/avatar/813.jpeg",
            rating = 4.5f,
            classCount = 12
        ),
        Academy(
            name = "Dance Studio",
            location = "Avenida Libertad 45, Barcelona",
            imageUrl = "https://rickandmortyapi.com/api/character/avatar/23.jpeg",
            rating = 4.2f,
            classCount = 8
        ),
        Academy(
            name = "Ritmo y Baile",
            location = "Plaza Central 7, Valencia",
            imageUrl = "https://rickandmortyapi.com/api/character/avatar/43.jpeg",
            rating = 4.8f,
            classCount = 15
        )
    ))
    val academies: StateFlow<List<Academy>> = _academies.asStateFlow()

    private val _invitations = MutableStateFlow(listOf(
        Invitation(
            academyName = "Academia de Salsa",
            academyImage = "https://rickandmortyapi.com/api/character/avatar/813.jpeg",
            invitationDate = "15/04/2024"
        ),
        Invitation(
            academyName = "Dance Studio",
            academyImage = "https://rickandmortyapi.com/api/character/avatar/23.jpeg",
            invitationDate = "16/04/2024"
        )
    ))
    val invitations: StateFlow<List<Invitation>> = _invitations.asStateFlow()

    fun onTabSelected(index: Int) {
        _selectedTab.value = index
    }

    fun acceptInvitation(invitation: Invitation) {
        _invitations.value = _invitations.value.filter { it != invitation }
        // TODO: Lógica para aceptar la invitación
    }

    fun rejectInvitation(invitation: Invitation) {
        _invitations.value = _invitations.value.filter { it != invitation }
        // TODO: Lógica para rechazar la invitación
    }
}