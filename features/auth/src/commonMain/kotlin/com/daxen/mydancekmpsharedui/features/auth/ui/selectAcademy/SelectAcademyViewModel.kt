package com.daxen.mydancekmpsharedui.features.auth.ui.selectAcademy

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SelectAcademyViewModel : ViewModel() {
    private val _selectedOption = MutableStateFlow(SelectAcademyOption.CODE)
    val selectedOption: StateFlow<SelectAcademyOption> = _selectedOption.asStateFlow()

    private val _academyCode = MutableStateFlow("")
    val academyCode: StateFlow<String> = _academyCode.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _academies = MutableStateFlow<List<AcademyItem>>(emptyList())
    val academies: StateFlow<List<AcademyItem>> = _academies.asStateFlow()

    fun updateSelectedOption(option: SelectAcademyOption) {
        _selectedOption.value = option
    }

    fun updateAcademyCode(code: String) {
        _academyCode.value = code
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        // TODO: Implementar búsqueda de academias
    }
}

enum class SelectAcademyOption {
    CODE, SEARCH
}

data class AcademyItem(
    val id: String,
    val name: String,
    val location: String
)