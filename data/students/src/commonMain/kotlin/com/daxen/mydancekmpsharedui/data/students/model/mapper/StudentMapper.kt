package com.daxen.mydancekmpsharedui.data.students.model.mapper

import com.daxen.mydancekmpsharedui.core.firebase.academy.students.model.StudentModel
import com.daxen.mydancekmpsharedui.data.students.model.DanceRole
import com.daxen.mydancekmpsharedui.data.students.model.Student

fun StudentModel.toStudent(): Student {
    val danceRole = DanceRole.from(this.danceRole)
        ?: DanceRole.LEADER

    return Student(
        uid = this.uid,
        email = this.email,
        name = this.name,
        lastName = this.lastName,
        birthDate = this.birthDate,
        phoneNumber = this.phoneNumber,
        phoneNumberPrefix = this.phoneNumberPrefix,
        danceRole = danceRole
    )
}