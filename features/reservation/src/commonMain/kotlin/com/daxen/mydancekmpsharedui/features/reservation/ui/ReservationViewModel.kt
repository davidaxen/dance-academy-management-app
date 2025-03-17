package com.daxen.mydancekmpsharedui.features.reservation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daxen.mydancekmpsharedui.data.classes.model.ClassModel
import com.daxen.mydancekmpsharedui.data.classes.repository.ClassesRepository
import com.daxen.mydancekmpsharedui.data.user.model.User
import com.daxen.mydancekmpsharedui.data.user.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

internal class ReservationViewModel(
    private val userRepository: UserRepository,
    private val classesRepository: ClassesRepository
): ViewModel() {
        val currentUser: StateFlow<User> = userRepository.currentUser

        private val _classesListState = MutableStateFlow<ClassesListUiState>(ClassesListUiState.Loading)
        val classesListState: StateFlow<ClassesListUiState> get() = _classesListState

        init {
            getClasses()
        }

        fun getClasses() {
            viewModelScope.launch {
                classesRepository.getClassesByAcademyId("ACADEMYID1")

                try {
                    classesRepository.classesList.collect { classList ->
                        when (classList) {
                            null -> {
                                _classesListState.value = ClassesListUiState.Loading
                            }
                            emptyList<ClassModel>() -> {
                                _classesListState.value = ClassesListUiState.Empty
                            }
                            else -> {
                                _classesListState.value = ClassesListUiState.Success(classList)
                            }
                        }
                    }
                } catch (e: Exception) {
                    _classesListState.value = ClassesListUiState.Error
                }
            }
        }


}