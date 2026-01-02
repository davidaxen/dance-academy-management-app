package com.daxen.mydancekmpsharedui.features.user.ui.sections.personalData

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.daxen.mydancekmpsharedui.data.user.model.User
import com.daxen.mydancekmpsharedui.data.user.repository.UserRepository
import kotlinx.coroutines.flow.StateFlow

class PersonalDataViewModel(
    userRepository: UserRepository
): ViewModel() {
    private val currentUser: StateFlow<User> = userRepository.currentUser
    var name by mutableStateOf("")
        private set
    var email by mutableStateOf("")
        private set

    init {
        name = currentUser.value.name
        email = currentUser.value.email
    }

    fun updateName(input: String) {
        name = input
    }

    fun updateEmail(input: String) {
        email = input
    }

}