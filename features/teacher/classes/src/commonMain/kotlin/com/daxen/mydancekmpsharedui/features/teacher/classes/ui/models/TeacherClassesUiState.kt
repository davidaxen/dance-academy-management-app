package com.daxen.mydancekmpsharedui.features.teacher.classes.ui.models

import com.daxen.mydancekmpsharedui.features.teacher.classes.ui.composables.ClassesGroup

sealed class TeacherClassesUiState {
    data object Loading : TeacherClassesUiState()
    
    data class Success(
        val classesGroups: List<ClassesGroup>
    ) : TeacherClassesUiState()
    
    data class Empty(
        val isFiltered: Boolean = false
    ) : TeacherClassesUiState()
    
    data class Error(
        val message: String
    ) : TeacherClassesUiState()
} 