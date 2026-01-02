package com.daxen.mydancekmpsharedui.core.firebase.academy.classes.model

interface ClassModel {
    val id: String
    val name: String
    val hour: String
    val status: String
    val teachers: List<TeacherInfo>
} 