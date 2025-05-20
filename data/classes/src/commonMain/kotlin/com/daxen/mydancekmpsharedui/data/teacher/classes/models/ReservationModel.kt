package com.daxen.mydancekmpsharedui.data.teacher.classes.models

data class ReservationModel(
    val userId: String,
    val classId: String,
    val date: String,
    val className: String,
    val academyId: String,
    val hour: String,
    val documentId: String = ""
) 