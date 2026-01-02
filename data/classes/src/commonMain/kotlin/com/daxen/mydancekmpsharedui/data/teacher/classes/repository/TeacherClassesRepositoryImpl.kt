package com.daxen.mydancekmpsharedui.data.teacher.classes.repository

import com.daxen.mydancekmpsharedui.core.firebase.academy.classes.repository.ClassRepository
import com.daxen.mydancekmpsharedui.data.academy.classes.models.SpecificClassModel
import com.daxen.mydancekmpsharedui.data.academy.classes.models.WeeklyClassModel
import com.daxen.mydancekmpsharedui.data.academy.classes.models.mapper.toSpecificClassModel
import com.daxen.mydancekmpsharedui.data.academy.classes.models.mapper.toWeeklyClassModel
import com.daxen.mydancekmpsharedui.data.teacher.classes.models.StudentReservation
import com.daxen.mydancekmpsharedui.data.teacher.classes.models.mapper.toStudentReservation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.LocalDate

class TeacherClassesRepositoryImpl(
    private val classRepository: ClassRepository
) : TeacherClassesRepository {
    private val _weeklyClassesList = MutableStateFlow<List<WeeklyClassModel>>(emptyList())
    override val weeklyClassesList: StateFlow<List<WeeklyClassModel>> = _weeklyClassesList

    private val _specificClassesList = MutableStateFlow<List<SpecificClassModel>>(emptyList())
    override val specificClassesList: StateFlow<List<SpecificClassModel>> = _specificClassesList

    override suspend fun getWeeklyClassesByTeacherAndAcademyId(
        teacherId: String,
        academyId: String,
        date: LocalDate
    ): List<WeeklyClassModel> {
        // Primero obtenemos todas las clases de la academia
        val allClasses = classRepository.getWeeklyClassesByAcademyId(academyId)
            .map { it.toWeeklyClassModel() }

        // Filtramos las clases donde el profesor es el teacherId
        // y coincide con el día de la semana seleccionado
        val selectedDayOfWeek = date.dayOfWeek.toString()
        val teacherClasses = allClasses.filter { weeklyClass ->
            weeklyClass.teachers.any { it.id == teacherId } &&
            weeklyClass.dayOfWeek == selectedDayOfWeek
        }

        _weeklyClassesList.value = teacherClasses
        return teacherClasses
    }

    override suspend fun getSpecificClassesByTeacherAndAcademyId(
        teacherId: String,
        academyId: String,
        date: LocalDate
    ): List<SpecificClassModel> {
        // Primero obtenemos todas las clases específicas de la academia
        val allClasses = classRepository.getSpecificClassesByAcademyId(academyId)
            .map { it.toSpecificClassModel() }

        // Filtramos las clases donde el profesor es el teacherId
        // y coincide con la fecha exacta seleccionada
        val dateString = "${date.year}-${date.monthNumber.toString().padStart(2, '0')}-${date.dayOfMonth.toString().padStart(2, '0')}"

        val teacherClasses = allClasses.filter { specificClass ->
            specificClass.teachers.any { it.id == teacherId } &&
            specificClass.date == dateString
        }

        _specificClassesList.value = teacherClasses
        return teacherClasses
    }

    override suspend fun getSpecificClassById(
        academyId: String,
        classId: String
    ): SpecificClassModel {
        val specificClass = classRepository.getSpecificClassById(academyId, classId)
        return specificClass.fold(
            onSuccess = { it.toSpecificClassModel() },
            onFailure = { throw it }
        )
    }

    override suspend fun getWeeklyClassById(
        academyId: String,
        classId: String
    ): WeeklyClassModel {
        val weeklyClass = classRepository.getWeeklyClassById(academyId, classId)
        return weeklyClass.fold(
            onSuccess = { it.toWeeklyClassModel() },
            onFailure = { throw it }
        )
    }
    
    override suspend fun getStudentReservationsByClassAndDate(
        academyId: String,
        classId: String,
        date: String
    ): List<StudentReservation> {
        val result = classRepository.getStudentReservationsByClassAndDate(
            academyId = academyId,
            classId = classId,
            date = date
        )
        
        return result.fold(
            onSuccess = { reservations ->
                reservations.map { it.toStudentReservation() }
            },
            onFailure = { emptyList() }
        )
    }
}