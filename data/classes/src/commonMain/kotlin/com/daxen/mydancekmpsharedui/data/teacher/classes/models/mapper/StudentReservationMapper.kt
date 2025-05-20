package com.daxen.mydancekmpsharedui.data.teacher.classes.models.mapper

import com.daxen.mydancekmpsharedui.core.firebase.academy.classes.model.ReservationAcademyModel as FirebaseReservationModel
import com.daxen.mydancekmpsharedui.core.firebase.academy.classes.model.StudentInfoReservation as FirebaseStudentInfoReservation
import com.daxen.mydancekmpsharedui.core.firebase.academy.classes.model.StudentReservation as FirebaseStudentReservation
import com.daxen.mydancekmpsharedui.data.teacher.classes.models.ReservationModel
import com.daxen.mydancekmpsharedui.data.teacher.classes.models.StudentInfoReservation
import com.daxen.mydancekmpsharedui.data.teacher.classes.models.StudentReservation

fun FirebaseStudentInfoReservation.toStudentInfoReservation(): StudentInfoReservation {
    return StudentInfoReservation(
        uid = uid,
        name = name,
        lastName = lastName,
        danceRole = danceRole
    )
}

fun FirebaseReservationModel.toReservationModel(): ReservationModel {
    return ReservationModel(
        userId = userId,
        classId = classId,
        date = date,
        className = className,
        academyId = academyId,
        hour = hour,
        documentId = documentId
    )
}

fun FirebaseStudentReservation.toStudentReservation(): StudentReservation {
    return StudentReservation(
        studentInfo = studentInfo.toStudentInfoReservation(),
        reservation = reservation.toReservationModel()
    )
} 