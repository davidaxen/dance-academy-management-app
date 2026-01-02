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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StudentsReservationListViewModel(
    private val teacherClassesRepository: TeacherClassesRepository,
    userRepository: UserRepository
): ViewModel() {
    private val currentUser: StateFlow<User> = userRepository.currentUser
    
    private val _state = MutableStateFlow(StudentsReservationListState())
    val state: StateFlow<StudentsReservationListState> = _state.asStateFlow()
    
    private var allStudents: List<StudentReservation> = emptyList()
    
    fun getStudentReservations(classId: String, date: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

            try {
                allStudents = teacherClassesRepository.getStudentReservationsByClassAndDate(
                    academyId = currentUser.value.currentAcademyId,
                    classId = classId,
                    date = date
                )
                applyFilters()
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message, isLoading = false)
            }
        }
    }
    
    fun onSearchQueryChanged(query: String) {
        _state.update { it.copy(searchQuery = query) }
        applyFilters()
    }
    
    fun onDanceRoleFilterChanged(danceRoleFilter: DanceRoleFilter) {
        _state.update { it.copy(danceRoleFilter = danceRoleFilter) }
        applyFilters()
    }
    
    private fun applyFilters() {
        val searchQuery = _state.value.searchQuery.lowercase()
        val danceRoleFilter = _state.value.danceRoleFilter
        
        val filteredStudents = allStudents.filter { student ->
            val matchesSearch = searchQuery.isEmpty() || 
                "${student.studentInfo.name} ${student.studentInfo.lastName}".lowercase().contains(searchQuery)
            
            val matchesRole = when (danceRoleFilter) {
                DanceRoleFilter.ALL -> true
                DanceRoleFilter.LEADER -> student.studentInfo.danceRole.equals("LEADER", ignoreCase = true)
                DanceRoleFilter.FOLLOWER -> student.studentInfo.danceRole.equals("FOLLOWER", ignoreCase = true)
            }
            
            matchesSearch && matchesRole
        }
        
        _state.value = _state.value.copy(students = filteredStudents, isLoading = false)
    }
}

enum class DanceRoleFilter {
    ALL, LEADER, FOLLOWER
}

data class StudentsReservationListState(
    val students: List<StudentReservation> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val danceRoleFilter: DanceRoleFilter = DanceRoleFilter.ALL
)