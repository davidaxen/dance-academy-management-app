package com.daxen.mydancekmpsharedui.features.user.ui.academySelection

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class Academy(
    val name: String,
    val location: String,
    val imageUrl: String,
    val schedule: String,
)

data class AcademyInvitation(
    val name: String,
    val imageUrl: String,
    val date: String
)

class AcademySelectionViewModel: ViewModel() {
    private val _selectedTab = MutableStateFlow(0)
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    private val _academies = MutableStateFlow(listOf(
        Academy(
            name = "RLDance",
            location = "Calle Mayor 123, Madrid",
            imageUrl = "https://rldancemadrid.my.canva.site/_assets/media/81ffe263615d3538218ffc9f14d339fe.png",
            schedule = "18:00 - 23:00",
        ),
        Academy(
            name = "Dance Studio",
            location = "Avenida Libertad 45, Barcelona",
            imageUrl = "https://scontent-mad1-1.xx.fbcdn.net/v/t39.30808-6/458941987_1026426806150373_6705277361731783958_n.jpg?_nc_cat=105&ccb=1-7&_nc_sid=6ee11a&_nc_ohc=2Z1vSoUPFL0Q7kNvwEx-b6I&_nc_oc=AdnRE-2GbVyBevZRjS0B3mrws2QTL9SLCGzScmv5ihuu94kqYoLnmY-DpPgzWPghkQ1asC0Ng2pX5k3uEhVlkzf7&_nc_zt=23&_nc_ht=scontent-mad1-1.xx&_nc_gid=HPoB6SrIDxCo3hA9cKY63w&oh=00_AfE1p5XlKAkFowVBEe3i4gwt11LQ2-9lQQ8Gpquci_fybw&oe=6817D5F5",
            schedule = "09:00 - 22:00",
        ),
    ))
    val academies: StateFlow<List<Academy>> = _academies.asStateFlow()

    private val _invitations = MutableStateFlow(listOf(
        AcademyInvitation(
            name = "Academia de Salsa",
            imageUrl = "https://rldancemadrid.my.canva.site/_assets/media/81ffe263615d3538218ffc9f14d339fe.png",
            date = "15/04/2024"
        ),
        AcademyInvitation(
            name = "Dance Studio",
            imageUrl = "https://scontent-mad1-1.xx.fbcdn.net/v/t39.30808-6/458941987_1026426806150373_6705277361731783958_n.jpg?_nc_cat=105&ccb=1-7&_nc_sid=6ee11a&_nc_ohc=2Z1vSoUPFL0Q7kNvwEx-b6I&_nc_oc=AdnRE-2GbVyBevZRjS0B3mrws2QTL9SLCGzScmv5ihuu94kqYoLnmY-DpPgzWPghkQ1asC0Ng2pX5k3uEhVlkzf7&_nc_zt=23&_nc_ht=scontent-mad1-1.xx&_nc_gid=HPoB6SrIDxCo3hA9cKY63w&oh=00_AfE1p5XlKAkFowVBEe3i4gwt11LQ2-9lQQ8Gpquci_fybw&oe=6817D5F5",
            date = "16/04/2024"
        )
    ))
    val invitations: StateFlow<List<AcademyInvitation>> = _invitations.asStateFlow()

    private val _loadingInvitation = MutableStateFlow<AcademyInvitation?>(null)
    val loadingInvitation: StateFlow<AcademyInvitation?> = _loadingInvitation.asStateFlow()

    fun onTabSelected(index: Int) {
        _selectedTab.value = index
    }

    fun acceptInvitation(academyInvitation: AcademyInvitation) {
        _loadingInvitation.value = academyInvitation
        // Simulamos una carga
        MainScope().launch {
            delay(2000) // Simulamos una operación de red
            _invitations.value = _invitations.value.filter { it != academyInvitation }
            _loadingInvitation.value = null
        }
    }

    fun rejectInvitation(academyInvitation: AcademyInvitation) {
        _loadingInvitation.value = academyInvitation
        // Simulamos una carga
        MainScope().launch {
            delay(2000) // Simulamos una operación de red
            _invitations.value = _invitations.value.filter { it != academyInvitation }
            _loadingInvitation.value = null
        }
    }
}