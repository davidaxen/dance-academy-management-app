package com.daxen.mydancekmpsharedui

import com.daxen.mydancekmpsharedui.features.academy.classes.academyClassesModule
import com.daxen.mydancekmpsharedui.features.academy.students.academyStudentsModule
import com.daxen.mydancekmpsharedui.features.academy.teachers.academyTeachersModule
import com.daxen.mydancekmpsharedui.features.auth.authModule
import com.daxen.mydancekmpsharedui.features.student.calendar.calendarModule
import com.daxen.mydancekmpsharedui.features.student.reservation.reservationModule
import com.daxen.mydancekmpsharedui.features.user.userModule
import org.koin.dsl.module

val appModule = module {
    includes(
        authModule,
        userModule,
        reservationModule,
        calendarModule,
        academyStudentsModule,
        academyTeachersModule,
        academyClassesModule
    )
}