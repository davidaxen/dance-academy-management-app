package com.daxen.mydancekmpsharedui.features.teacher.classes

import com.daxen.mydancekmpsharedui.data.academy.classes.dataAcademyClassesModule
import com.daxen.mydancekmpsharedui.data.teacher.classes.dataTeacherClassesModule
import com.daxen.mydancekmpsharedui.data.user.dataUserModule
import com.daxen.mydancekmpsharedui.features.teacher.classes.viewmodel.ClassDetailViewModel
import com.daxen.mydancekmpsharedui.features.teacher.classes.viewmodel.StudentsReservationListViewModel
import com.daxen.mydancekmpsharedui.features.teacher.classes.viewmodel.TeacherClassesViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val teacherClassesModule = module {
    includes(dataAcademyClassesModule)
    includes(dataUserModule)
    includes(dataTeacherClassesModule)

    viewModelOf(::TeacherClassesViewModel)
    viewModelOf(::ClassDetailViewModel)
    viewModelOf(::StudentsReservationListViewModel)
}