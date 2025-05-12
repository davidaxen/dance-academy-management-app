package com.daxen.mydancekmpsharedui.features.user.ui.academyUser.sections.academyData

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.daxen.mydancekmpsharedui.data.user.model.UserAcademy
import com.daxen.mydancekmpsharedui.data.user.repository.AcademyUserRepository
import kotlinx.coroutines.flow.StateFlow

class AcademyDataViewModel(
    academyUserRepository: AcademyUserRepository
): ViewModel() {
    private val currentAcademy: StateFlow<UserAcademy> = academyUserRepository.currentAcademy
    var name by mutableStateOf("")
        private set
    var email by mutableStateOf("")
        private set
    var nif by mutableStateOf("")
        private set
    var address by mutableStateOf("")
        private set
    var openingTime by mutableStateOf("")
        private set
    var closingTime by mutableStateOf("")
        private set

    init {
        name = currentAcademy.value.name
        email = currentAcademy.value.email
        nif = currentAcademy.value.nif
        address = currentAcademy.value.address
        openingTime = currentAcademy.value.openingTime
        closingTime = currentAcademy.value.closingTime
    }

    fun updateName(input: String) {
        name = input
    }

    fun updateEmail(input: String) {
        email = input
    }

    fun updateNif(input: String) {
        nif = input
    }

    fun updateAddress(input: String) {
        address = input
    }

    fun updateOpeningTime(input: String) {
        openingTime = input
    }

    fun updateClosingTime(input: String) {
        closingTime = input
    }
} 