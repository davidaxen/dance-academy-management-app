package com.daxen.mydancekmpsharedui.features.academy.classes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daxen.mydancekmpsharedui.data.academy.classes.models.SpecificClassModel
import com.daxen.mydancekmpsharedui.data.academy.classes.models.WeeklyClassModel
import com.daxen.mydancekmpsharedui.data.academy.classes.repository.AcademyClassesRepository
import com.daxen.mydancekmpsharedui.data.user.model.UserAcademy
import com.daxen.mydancekmpsharedui.data.user.repository.AcademyUserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ClassDetailViewModel(
    private val academyClassesRepository: AcademyClassesRepository,
    academyUserRepository: AcademyUserRepository
) : ViewModel() {
    
    private val currentAcademy: StateFlow<UserAcademy> = academyUserRepository.currentAcademy
    
    private val _uiState = MutableStateFlow(ClassDetailUiState())
    val uiState: StateFlow<ClassDetailUiState> = _uiState.asStateFlow()
    
    private val _showDeleteConfirmation = MutableStateFlow(false)
    val showDeleteConfirmation: StateFlow<Boolean> = _showDeleteConfirmation
    
    fun loadClass(classId: String, isWeekly: Boolean) {
        _uiState.update { it.copy(isLoading = true, error = null) }
        
        viewModelScope.launch {
            try {
                val academyId = currentAcademy.value.academyId
                
                if (isWeekly) {
                    val weeklyClasses = academyClassesRepository.getWeeklyClassesByAcademyId(academyId)
                    val weeklyClass = weeklyClasses.find { it.id == classId }
                    weeklyClass?.let {
                        _uiState.update { state ->
                            state.copy(
                                isLoading = false,
                                weeklyClass = weeklyClass,
                                isWeeklyClass = true
                            )
                        }
                    } ?: run {
                        _uiState.update { it.copy(
                            isLoading = false,
                            error = "No se encontró la clase"
                        ) }
                    }
                } else {
                    val specificClasses = academyClassesRepository.getSpecificClassesByAcademyId(academyId)
                    val specificClass = specificClasses.find { it.id == classId }
                    specificClass?.let {
                        _uiState.update { state ->
                            state.copy(
                                isLoading = false,
                                specificClass = specificClass,
                                isWeeklyClass = false
                            )
                        }
                    } ?: run {
                        _uiState.update { it.copy(
                            isLoading = false,
                            error = "No se encontró la clase"
                        ) }
                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    isLoading = false,
                    error = e.message ?: "Error al cargar los datos de la clase"
                ) }
            }
        }
    }
    
    fun showDeleteConfirmationDialog() {
        _showDeleteConfirmation.value = true
    }
    
    fun hideDeleteConfirmationDialog() {
        _showDeleteConfirmation.value = false
    }
    
    fun deleteClass() {
        val state = _uiState.value
        
        if (state.isLoading) return
        
        _uiState.update { it.copy(isLoading = true) }
        
        viewModelScope.launch {
            try {
                val academyId = currentAcademy.value.academyId
                
                // TODO: Implementar la función de borrar clase en el repositorio
                // Por ahora, simularemos que se borró correctamente
                
                _uiState.update { it.copy(
                    isLoading = false,
                    classDeleted = true
                ) }
                
                _showDeleteConfirmation.value = false
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    isLoading = false,
                    error = e.message ?: "Error al eliminar la clase"
                ) }
                
                _showDeleteConfirmation.value = false
            }
        }
    }
}

data class ClassDetailUiState(
    val isLoading: Boolean = true,
    val weeklyClass: WeeklyClassModel? = null,
    val specificClass: SpecificClassModel? = null,
    val isWeeklyClass: Boolean = true,
    val error: String? = null,
    val classDeleted: Boolean = false
) 