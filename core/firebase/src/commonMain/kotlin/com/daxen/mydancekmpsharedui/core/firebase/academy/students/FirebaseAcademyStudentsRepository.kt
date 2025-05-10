package com.daxen.mydancekmpsharedui.core.firebase.academy.students

import com.daxen.mydancekmpsharedui.core.firebase.academy.students.model.StudentModel

interface FirebaseAcademyStudentsRepository {
    suspend fun getStudentsByAcademyID(id: String): List<StudentModel>
    suspend fun inviteStudentToAcademy(): Boolean
}