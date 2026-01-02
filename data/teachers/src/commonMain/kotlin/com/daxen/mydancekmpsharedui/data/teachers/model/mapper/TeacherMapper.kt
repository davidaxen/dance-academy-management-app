package com.daxen.mydancekmpsharedui.data.teachers.model.mapper

import com.daxen.mydancekmpsharedui.core.firebase.academy.teachers.model.TeacherModel
import com.daxen.mydancekmpsharedui.data.teachers.model.Teacher

fun TeacherModel.toTeacher(): Teacher {
    return Teacher(
        uid = this.uid,
        name = this.name,
        lastName = this.lastName,
        email = this.email,
        birthDate = this.birthDate,
        phoneNumber = this.phoneNumber,
        phoneNumberPrefix = this.phoneNumberPrefix,
        profileImageUrl = this.profileImageUrl,
        joinedAt = this.joinedAt
    )
}

fun Teacher.toFirebaseModel(): TeacherModel {
    return TeacherModel(
        uid = this.uid,
        name = this.name,
        lastName = this.lastName,
        email = this.email,
        birthDate = this.birthDate,
        phoneNumber = this.phoneNumber,
        phoneNumberPrefix = this.phoneNumberPrefix,
        profileImageUrl = this.profileImageUrl,
        joinedAt = this.joinedAt
    )
} 