package com.daxen.mydancekmpsharedui.features.reservation.ui

import com.daxen.mydancekmpsharedui.data.classes.model.ClassModel

sealed class ClassesListUiState {
    data object Loading : ClassesListUiState()
    data object Empty : ClassesListUiState()
    data class Success(val classes: List<ClassModel>) : ClassesListUiState()
    data object Error : ClassesListUiState()
}