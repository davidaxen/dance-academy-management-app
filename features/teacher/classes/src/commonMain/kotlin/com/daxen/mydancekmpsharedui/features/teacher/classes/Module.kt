package com.daxen.mydancekmpsharedui.features.teacher.classes

import com.daxen.mydancekmpsharedui.features.teacher.classes.viewmodel.TeacherClassesViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val teacherClassesModule = module {
    viewModelOf(::TeacherClassesViewModel)
}