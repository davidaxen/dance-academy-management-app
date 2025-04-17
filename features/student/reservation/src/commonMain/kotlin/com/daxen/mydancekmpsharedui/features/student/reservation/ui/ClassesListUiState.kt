package com.daxen.mydancekmpsharedui.features.student.reservation.ui

import com.daxen.mydancekmpsharedui.data.classes.models.SpecificClassModel
import com.daxen.mydancekmpsharedui.data.classes.models.WeeklyClassModel

sealed class ClassesListUiState {
    data object Loading : ClassesListUiState()
    data object Empty : ClassesListUiState()
    data object Error : ClassesListUiState()
    data class Success(
        val weekly: List<WeeklyClassModel>,
        val specific: List<SpecificClassModel>
    ) : ClassesListUiState()
}