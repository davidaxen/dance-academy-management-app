package com.daxen.mydancekmpsharedui.features.user.ui.academySelection

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class AcademySelectionViewModel: ViewModel() {
    var selectedTab by mutableStateOf(0)
        private set

    fun onTabSelected(index: Int) {
        selectedTab = index
    }
}