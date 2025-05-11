package com.daxen.mydancekmpsharedui.data.students.repository

import com.daxen.mydancekmpsharedui.data.students.model.Student
import kotlinx.coroutines.flow.StateFlow

interface AcademyStudentsRepository {
    val students: StateFlow<List<Student>>
    suspend fun getStudentsByAcademyID(id: String)
    suspend fun inviteStudentToAcademy(academyId: String, studentId: String, academyName: String): Boolean
}