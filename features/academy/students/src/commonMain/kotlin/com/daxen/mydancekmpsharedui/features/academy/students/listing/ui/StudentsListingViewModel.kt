package com.daxen.mydancekmpsharedui.features.academy.students.listing.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daxen.mydancekmpsharedui.data.students.model.Student
import com.daxen.mydancekmpsharedui.data.students.repository.AcademyStudentsRepository
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

object StudentsListingViewModelProvider : KoinComponent {
    private val viewModel: StudentsListingViewModel by inject()
    
    fun get(): StudentsListingViewModel = viewModel
}

// Estado UI para la pantalla de listado de estudiantes
sealed class StudentsListingUiState {
    data object Loading : StudentsListingUiState()
    data class Success(val students: List<Student>) : StudentsListingUiState()
    data class Empty(val isFiltered: Boolean) : StudentsListingUiState() // isFiltered indica si está vacío por búsqueda
    data class Error(val message: String) : StudentsListingUiState() // Nuevo estado para errores
}

class StudentsListingViewModel(
    private val studentsRepository: AcademyStudentsRepository,
    academyUserRepository: AcademyUserRepository
) : ViewModel() {
    private val currentAcademy: StateFlow<UserAcademy> = academyUserRepository.currentAcademy

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    private val _error = MutableStateFlow<String?>(null)
    
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()
    
    private val _students: StateFlow<List<Student>> = studentsRepository.students

    private val filteredStudents = combine(
        _students, _searchQuery
    ) { students, query ->
        if (query.isBlank()) students
        else {
            val lowerQuery = query.lowercase()
            students.filter {
                it.name.contains(lowerQuery, ignoreCase = true) ||
                        it.lastName.contains(lowerQuery, ignoreCase = true) ||
                        it.email.contains(lowerQuery, ignoreCase = true)
            }
        }
    }
    
    val uiState: StateFlow<StudentsListingUiState> = combine(
        filteredStudents, _isLoading, _searchQuery, _error
    ) { filteredList, isLoading, query, error ->
        when {
            isLoading && !_isRefreshing.value -> StudentsListingUiState.Loading
            error != null -> StudentsListingUiState.Error(error)
            filteredList.isEmpty() -> StudentsListingUiState.Empty(isFiltered = query.isNotEmpty())
            else -> StudentsListingUiState.Success(filteredList)
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        StudentsListingUiState.Loading
    )

    init {
        loadStudents()
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    private fun loadStudents() {
        viewModelScope.launch {
            if (!_isRefreshing.value) {
                _isLoading.value = true
            }
            _error.value = null
            
            try {
                studentsRepository.getStudentsByAcademyID(currentAcademy.value.academyId)
            } catch (e: Exception) {
                _error.value = "Error al obtener los estudiantes: ${e.message}"
            } finally {
                _isLoading.value = false
                _isRefreshing.value = false
            }
        }
    }
    
    fun refreshStudents() {
        _isRefreshing.value = true
        loadStudents()
    }
}