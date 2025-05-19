package com.daxen.mydancekmpsharedui.features.teacher.classes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daxen.mydancekmpsharedui.data.academy.classes.models.SpecificClassModel
import com.daxen.mydancekmpsharedui.data.academy.classes.models.WeeklyClassModel
import com.daxen.mydancekmpsharedui.data.teacher.classes.repository.TeacherClassesRepository
import com.daxen.mydancekmpsharedui.data.user.model.User
import com.daxen.mydancekmpsharedui.data.user.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ClassDetailViewModel(
    private val teacherClassesRepository: TeacherClassesRepository,
    userRepository: UserRepository
) : ViewModel() {
    
    private val currentUser: StateFlow<User> = userRepository.currentUser
    
    private val _uiState = MutableStateFlow(ClassDetailUiState())
    val uiState: StateFlow<ClassDetailUiState> = _uiState.asStateFlow()
    
    fun loadClass(classId: String, isWeekly: Boolean) {
        _uiState.update { it.copy(isLoading = true, error = null) }
        
        viewModelScope.launch {
            try {
                val academyId = currentUser.value.currentAcademyId
                
                if (isWeekly) {
                    val weeklyClass = teacherClassesRepository.getWeeklyClassById(academyId, classId)
                    weeklyClass.let {
                        _uiState.update { state ->
                            state.copy(
                                isLoading = false,
                                weeklyClass = weeklyClass,
                                isWeeklyClass = true
                            )
                        }
                    }
                } else {
                    val specificClass = teacherClassesRepository.getSpecificClassById(academyId, classId)
                    specificClass.let {
                        _uiState.update { state ->
                            state.copy(
                                isLoading = false,
                                specificClass = specificClass,
                                isWeeklyClass = false
                            )
                        }
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
}

data class ClassDetailUiState(
    val isLoading: Boolean = true,
    val weeklyClass: WeeklyClassModel? = null,
    val specificClass: SpecificClassModel? = null,
    val isWeeklyClass: Boolean = true,
    val error: String? = null
) 