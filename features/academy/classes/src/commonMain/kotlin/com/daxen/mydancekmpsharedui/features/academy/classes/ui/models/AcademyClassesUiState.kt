package com.daxen.mydancekmpsharedui.features.academy.classes.ui.models

import com.daxen.mydancekmpsharedui.features.academy.classes.ui.composables.ClassesGroup

sealed class AcademyClassesUiState {
    data object Loading : AcademyClassesUiState()
    
    data class Success(
        val classesGroups: List<ClassesGroup>
    ) : AcademyClassesUiState()
    
    data class Empty(
        val isFiltered: Boolean = false
    ) : AcademyClassesUiState()
    
    data class Error(
        val message: String
    ) : AcademyClassesUiState()
} 