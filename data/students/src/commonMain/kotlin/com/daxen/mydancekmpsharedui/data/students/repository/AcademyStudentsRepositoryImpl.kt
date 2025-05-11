package com.daxen.mydancekmpsharedui.data.students.repository

import com.daxen.mydancekmpsharedui.core.firebase.academy.students.FirebaseAcademyStudentsRepository
import com.daxen.mydancekmpsharedui.data.students.model.Student
import com.daxen.mydancekmpsharedui.data.students.model.mapper.toStudent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AcademyStudentsRepositoryImpl(
    private val firebaseAcademyStudentsRepository: FirebaseAcademyStudentsRepository
): AcademyStudentsRepository {
    private val _students: MutableStateFlow<List<Student>> = MutableStateFlow(emptyList())
    override val students: StateFlow<List<Student>> = _students

    override suspend fun getStudentsByAcademyID(id: String) {
        val studentsResponse = firebaseAcademyStudentsRepository.getStudentsByAcademyID(id)
        _students.value = studentsResponse.map { it.toStudent() }
    }

    override suspend fun inviteStudentToAcademy(academyId: String, studentId: String, academyName: String): Boolean {
        return firebaseAcademyStudentsRepository.inviteStudentToAcademy(
            userId = studentId,
            academyId = academyId,
            academyName = academyName
        )
    }
}