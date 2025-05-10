package com.daxen.mydancekmpsharedui.features.auth.academy.ui.academyInfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daxen.mydancekmpsharedui.data.user.repository.AcademyUserRepository
import com.daxen.mydancekmpsharedui.features.auth.utils.AcademyValidations
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AcademyInfoViewModel(
    private val academyUserRepository: AcademyUserRepository
): ViewModel() {
    private val _academyInfoState = MutableStateFlow<AcademyInfoState>(AcademyInfoState.Initial)
    val academyInfoState: StateFlow<AcademyInfoState> = _academyInfoState.asStateFlow()

    private val _name = MutableStateFlow("qeqweqweq")
    val name: StateFlow<String> = _name.asStateFlow()
    private val _nameError = MutableStateFlow<String?>(null)
    val nameError: StateFlow<String?> = _nameError.asStateFlow()

    private val _nif = MutableStateFlow("qweqweqwe")
    val nif: StateFlow<String> = _nif.asStateFlow()
    private val _nifError = MutableStateFlow<String?>(null)
    val nifError: StateFlow<String?> = _nifError.asStateFlow()

    private val _address = MutableStateFlow("qweqweqwe")
    val address: StateFlow<String> = _address.asStateFlow()
    private val _addressError = MutableStateFlow<String?>(null)
    val addressError: StateFlow<String?> = _addressError.asStateFlow()

    private val _openingTime = MutableStateFlow("03:22")
    val openingTime: StateFlow<String> = _openingTime.asStateFlow()
    private val _openingTimeError = MutableStateFlow<String?>(null)
    val openingTimeError: StateFlow<String?> = _openingTimeError.asStateFlow()

    private val _closingTime = MutableStateFlow("03:40")
    val closingTime: StateFlow<String> = _closingTime.asStateFlow()
    private val _closingTimeError = MutableStateFlow<String?>(null)
    val closingTimeError: StateFlow<String?> = _closingTimeError.asStateFlow()

    private val _isSubmitting = MutableStateFlow(false)
    val isSubmitting: StateFlow<Boolean> = _isSubmitting.asStateFlow()

    fun updateName(name: String) {
        _name.value = name
        _nameError.value = AcademyValidations.validateName(name)
    }

    fun updateLegalName(legalName: String) {
        _nif.value = legalName
        _nifError.value = AcademyValidations.validateNif(legalName)
    }

    fun updateAddress(address: String) {
        _address.value = address
        _addressError.value = AcademyValidations.validateAddress(address)
    }

    fun updateOpeningTime(time: String) {
        _openingTime.value = time
        _openingTimeError.value = AcademyValidations.validateOpeningTime(time)
    }

    fun updateClosingTime(time: String) {
        _closingTime.value = time
        _closingTimeError.value = AcademyValidations.validateClosingTime(time)
    }

    fun validateAndSubmit() {
        val nameError = AcademyValidations.validateName(_name.value)
        val nifError = AcademyValidations.validateNif(_nif.value)
        val addressError = AcademyValidations.validateAddress(_address.value)
        val openingTimeError = AcademyValidations.validateOpeningTime(_openingTime.value)
        val closingTimeError = AcademyValidations.validateClosingTime(_closingTime.value)

        _nameError.value = nameError
        _nifError.value = nifError
        _addressError.value = addressError
        _openingTimeError.value = openingTimeError
        _closingTimeError.value = closingTimeError

        if (nameError == null && nifError == null && addressError == null && 
            openingTimeError == null && closingTimeError == null) {
            _isSubmitting.value = true
            submitAcademyInfo()
        }
    }

    private fun submitAcademyInfo() {
        viewModelScope.launch {
            try {
                _academyInfoState.value = AcademyInfoState.Loading
                academyUserRepository.setAcademyInfo(
                    name = _name.value,
                    nif = _nif.value,
                    address = _address.value,
                    openingTime = _openingTime.value,
                    closingTime = _closingTime.value
                )
                _academyInfoState.value = AcademyInfoState.Success
            } catch (e: Exception) {
                _academyInfoState.value = AcademyInfoState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    fun onBackClicked() {
        _academyInfoState.value = AcademyInfoState.Initial
        _isSubmitting.value = false
    }
}