package com.daxen.mydancekmpsharedui.features.auth.academy.ui.logoUploader

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daxen.mydancekmpsharedui.data.auth.repository.AuthRepository
import com.daxen.mydancekmpsharedui.data.user.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class LogoUploaderState {
    data object Initial : LogoUploaderState()
    data object Loading : LogoUploaderState()
    data object Success : LogoUploaderState()
    data class Error(val message: String) : LogoUploaderState()
}

class LogoUploaderViewModel(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
): ViewModel() {
    private val _logoUploaderState = MutableStateFlow<LogoUploaderState>(LogoUploaderState.Initial)
    val logoUploaderState: StateFlow<LogoUploaderState> = _logoUploaderState.asStateFlow()

    private val _selectedImage = MutableStateFlow<ByteArray?>(null)
    val selectedImage: StateFlow<ByteArray?> = _selectedImage.asStateFlow()

    private val _isSubmitting = MutableStateFlow(false)
    val isSubmitting: StateFlow<Boolean> = _isSubmitting.asStateFlow()

    fun updateSelectedImage(imageBytes: ByteArray) {
        _selectedImage.value = imageBytes
    }

    fun removeSelectedImage() {
        _selectedImage.value = null
    }

    fun validateAndSubmit() {
        if (_selectedImage.value == null) {
            _logoUploaderState.value = LogoUploaderState.Error("Debes seleccionar una imagen")
            return
        }

        viewModelScope.launch {
            try {
                _isSubmitting.value = true
                _logoUploaderState.value = LogoUploaderState.Loading

                // TODO: Implementar la lógica de subida de imagen a Firebase Storage
                // Por ahora solo simulamos el éxito
                _logoUploaderState.value = LogoUploaderState.Success
            } catch (e: Exception) {
                _logoUploaderState.value = LogoUploaderState.Error(e.message ?: "Error desconocido")
            } finally {
                _isSubmitting.value = false
            }
        }
    }

    fun onBackClicked() {
        _logoUploaderState.value = LogoUploaderState.Initial
    }
}