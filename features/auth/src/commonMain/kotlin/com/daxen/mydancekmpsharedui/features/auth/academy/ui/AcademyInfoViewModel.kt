package com.daxen.mydancekmpsharedui.features.auth.academy.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daxen.mydancekmpsharedui.data.auth.repository.AuthRepository
import com.daxen.mydancekmpsharedui.data.user.repository.UserRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class AcademyInfoState {
    data object Initial : AcademyInfoState()
    data object Loading : AcademyInfoState()
    data object Success : AcademyInfoState()
    data class Error(val message: String) : AcademyInfoState()
}

class AcademyInfoViewModel(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
): ViewModel() {
    private val _academyInfoState = MutableStateFlow<AcademyInfoState>(AcademyInfoState.Initial)
    val academyInfoState: StateFlow<AcademyInfoState> = _academyInfoState.asStateFlow()

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name.asStateFlow()
    private val _nameError = MutableStateFlow<String?>(null)
    val nameError: StateFlow<String?> = _nameError.asStateFlow()

    private val _nif = MutableStateFlow("")
    val nif: StateFlow<String> = _nif.asStateFlow()

    private val _nifError = MutableStateFlow<String?>(null)
    val nifError: StateFlow<String?> = _nifError.asStateFlow()

    private val _address = MutableStateFlow("")
    val address: StateFlow<String> = _address.asStateFlow()
    private val _addressError = MutableStateFlow<String?>(null)
    val addressError: StateFlow<String?> = _addressError.asStateFlow()

    private val _openingTime = MutableStateFlow("")
    val openingTime: StateFlow<String> = _openingTime.asStateFlow()
    private val _openingTimeError = MutableStateFlow<String?>(null)
    val openingTimeError: StateFlow<String?> = _openingTimeError.asStateFlow()

    private val _closingTime = MutableStateFlow("")
    val closingTime: StateFlow<String> = _closingTime.asStateFlow()
    private val _closingTimeError = MutableStateFlow<String?>(null)
    val closingTimeError: StateFlow<String?> = _closingTimeError.asStateFlow()

    private val _isSubmitting = MutableStateFlow(false)
    val isSubmitting: StateFlow<Boolean> = _isSubmitting.asStateFlow()

    fun updateName(name: String) {
        _name.value = name
        _nameError.value = null
    }

    fun updateLegalName(legalName: String) {
        _nif.value = legalName
        _nifError.value = null
    }

    fun updateAddress(address: String) {
        _address.value = address
        _addressError.value = null
    }

    fun updateOpeningTime(time: String) {
        _openingTime.value = time
        _openingTimeError.value = null
    }

    fun updateClosingTime(time: String) {
        _closingTime.value = time
        _closingTimeError.value = null
    }

    fun validateAndSubmit() {
        var isValid = true

        if (_name.value.isBlank()) {
            _nameError.value = "El nombre es obligatorio"
            isValid = false
        }

        if (_nif.value.isBlank()) {
            _nifError.value = "El NIF es obligatorio"
            isValid = false
        }

        if (_address.value.isBlank()) {
            _addressError.value = "La dirección es obligatoria"
            isValid = false
        }

        if (_openingTime.value.isBlank()) {
            _openingTimeError.value = "La hora de apertura es obligatoria"
            isValid = false
        }

        if (_closingTime.value.isBlank()) {
            _closingTimeError.value = "La hora de cierre es obligatoria"
            isValid = false
        }

        if (isValid) {
            submitAcademyInfo()
        }
    }

    private fun submitAcademyInfo() {
        viewModelScope.launch {
            _isSubmitting.value = true
            _academyInfoState.value = AcademyInfoState.Loading

            try {
                // TODO: Implementar la lógica para guardar la información de la academia
                _academyInfoState.value = AcademyInfoState.Success
            } catch (e: Exception) {
                _academyInfoState.value = AcademyInfoState.Error(e.message ?: "Error desconocido")
            } finally {
                _isSubmitting.value = false
            }
        }
    }

    fun onBackClicked() {
        _academyInfoState.value = AcademyInfoState.Initial
    }
}