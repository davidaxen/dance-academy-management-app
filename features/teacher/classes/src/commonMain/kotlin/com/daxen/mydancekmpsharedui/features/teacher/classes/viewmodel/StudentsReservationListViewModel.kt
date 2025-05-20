package com.daxen.mydancekmpsharedui.features.teacher.classes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daxen.mydancekmpsharedui.data.teacher.classes.models.StudentReservation
import com.daxen.mydancekmpsharedui.data.teacher.classes.repository.TeacherClassesRepository
import com.daxen.mydancekmpsharedui.data.user.model.User
import com.daxen.mydancekmpsharedui.data.user.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StudentsReservationListViewModel(
    private val teacherClassesRepository: TeacherClassesRepository,
    userRepository: UserRepository
): ViewModel() {
    private val currentUser: StateFlow<User> = userRepository.currentUser
    
    private val _state = MutableStateFlow(StudentsReservationListState())
    val state: StateFlow<StudentsReservationListState> = _state.asStateFlow()
    
    fun getStudentReservations(classId: String, date: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

            try {
                val students = teacherClassesRepository.getStudentReservationsByClassAndDate(
                    academyId = currentUser.value.currentAcademyId,
                    classId = classId,
                    date = date
                )
                _state.value = _state.value.copy(students = students, isLoading = false)
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message, isLoading = false)
            }
        }
    }
}

data class StudentsReservationListState(
    val students: List<StudentReservation> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)