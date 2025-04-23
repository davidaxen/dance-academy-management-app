package com.daxen.mydancekmpsharedui.features.auth.ui.personalInfo

import androidx.lifecycle.ViewModel
import com.daxen.mydancekmpsharedui.data.user.repository.UserRepository
import com.daxen.mydancekmpsharedui.features.auth.utils.Validations
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PersonalInfoViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    val prefixOptions: List<String> = listOf("+34", "+1", "+58", "+52", "+33")

    private val _personalInfoState = MutableStateFlow<PersonalInfoState>(PersonalInfoState.Idle)
    val personalInfoState: StateFlow<PersonalInfoState> = _personalInfoState.asStateFlow()

    private val _firstName = MutableStateFlow("")
    val firstName: StateFlow<String> = _firstName.asStateFlow()
    
    private val _firstNameError = MutableStateFlow<String?>(null)
    val firstNameError: StateFlow<String?> = _firstNameError.asStateFlow()

    private val _lastName = MutableStateFlow("")
    val lastName: StateFlow<String> = _lastName.asStateFlow()
    
    private val _lastNameError = MutableStateFlow<String?>(null)
    val lastNameError: StateFlow<String?> = _lastNameError.asStateFlow()

    private val _birthDate = MutableStateFlow("")
    
    private val _birthDateError = MutableStateFlow<String?>(null)
    val birthDateError: StateFlow<String?> = _birthDateError.asStateFlow()

    private val _phone = MutableStateFlow("")
    val phone: StateFlow<String> = _phone.asStateFlow()
    
    private val _phoneError = MutableStateFlow<String?>(null)
    val phoneError: StateFlow<String?> = _phoneError.asStateFlow()

    private val _selectedPrefix = MutableStateFlow(prefixOptions.first())
    val selectedPrefix: StateFlow<String> = _selectedPrefix.asStateFlow()

    private val _isSubmitting = MutableStateFlow(false)
    val isSubmitting: StateFlow<Boolean> = _isSubmitting.asStateFlow()

    fun validateAndSubmit() {
        _firstNameError.value = Validations.validateFirstName(_firstName.value)
        _lastNameError.value = Validations.validateLastName(_lastName.value)
        _birthDateError.value = Validations.validateBirthDate(_birthDate.value)
        _phoneError.value = Validations.validatePhone(_phone.value)

        if (_firstNameError.value == null && _lastNameError.value == null && _birthDateError.value == null && _phoneError.value == null) {
            _isSubmitting.value = true
            onInfoSubmitted()
        } else {
            _isSubmitting.value = false
        }
    }

    private fun onInfoSubmitted() {
        _personalInfoState.value = PersonalInfoState.Loading
        userRepository.setPersonalInfo(
            firstName = _firstName.value,
            lastName = _lastName.value,
            birthDate = _birthDate.value,
            phone = _phone.value,
            prefix = _selectedPrefix.value
        )
        _personalInfoState.value = PersonalInfoState.Success
        _isSubmitting.value = false
    }

    fun updateFirstName(value: String) {
        _firstName.value = value
        _firstNameError.value = null
    }

    fun updateLastName(value: String) {
        _lastName.value = value
        _lastNameError.value = null
    }

    fun updateBirthDate(value: String) {
        _birthDate.value = value
        _birthDateError.value = null
    }

    fun updatePhone(value: String) {
        _phone.value = value
        _phoneError.value = null
    }

    fun updateSelectedPrefix(value: String) {
        _selectedPrefix.value = value
    }
}