package com.daxen.mydancekmpsharedui.features.academy.teachers.listing.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daxen.mydancekmpsharedui.data.teachers.model.Teacher
import com.daxen.mydancekmpsharedui.data.teachers.repository.AcademyTeachersRepository
import com.daxen.mydancekmpsharedui.data.user.model.UserAcademy
import com.daxen.mydancekmpsharedui.data.user.repository.AcademyUserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object TeachersListingViewModelProvider : KoinComponent {
    private val viewModel: TeachersListingViewModel by inject()
    
    fun get(): TeachersListingViewModel = viewModel
}

// Estado UI para la pantalla de listado de profesores
sealed class TeachersListingUiState {
    data object Loading : TeachersListingUiState()
    data class Success(val teachers: List<Teacher>) : TeachersListingUiState()
    data class Empty(val isFiltered: Boolean) : TeachersListingUiState() // isFiltered indica si está vacío por búsqueda
    data class Error(val message: String) : TeachersListingUiState() // Estado para errores
}

class TeachersListingViewModel(
    private val teachersRepository: AcademyTeachersRepository,
    academyUserRepository: AcademyUserRepository
) : ViewModel() {
    private val currentAcademy: StateFlow<UserAcademy> = academyUserRepository.currentAcademy

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    private val _error = MutableStateFlow<String?>(null)
    
    private val _teachers: StateFlow<List<Teacher>> = teachersRepository.teachers

    private val filteredTeachers = combine(
        _teachers, _searchQuery
    ) { teachers, query ->
        if (query.isBlank()) teachers
        else {
            val lowerQuery = query.lowercase()
            teachers.filter {
                it.name.contains(lowerQuery, ignoreCase = true) ||
                        it.lastName.contains(lowerQuery, ignoreCase = true) ||
                        it.email.contains(lowerQuery, ignoreCase = true)
            }
        }
    }
    
    val uiState: StateFlow<TeachersListingUiState> = combine(
        filteredTeachers, _isLoading, _searchQuery, _error
    ) { filteredList, isLoading, query, error ->
        when {
            isLoading -> TeachersListingUiState.Loading
            error != null -> TeachersListingUiState.Error(error)
            filteredList.isEmpty() -> TeachersListingUiState.Empty(isFiltered = query.isNotEmpty())
            else -> TeachersListingUiState.Success(filteredList)
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        TeachersListingUiState.Loading
    )

    init {
        loadTeachers()
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    private fun loadTeachers() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            try {
                teachersRepository.getTeachersByAcademyID(currentAcademy.value.academyId)
            } catch (e: Exception) {
                _error.value = "Error al obtener los profesores: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun refreshTeachers() {
        loadTeachers()
    }
} 