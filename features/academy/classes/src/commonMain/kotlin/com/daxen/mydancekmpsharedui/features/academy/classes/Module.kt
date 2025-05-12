package com.daxen.mydancekmpsharedui.features.academy.classes

import com.daxen.mydancekmpsharedui.data.academy.classes.dataAcademyClassesModule
import com.daxen.mydancekmpsharedui.data.teachers.dataTeachersModule
import com.daxen.mydancekmpsharedui.data.user.dataUserModule
import com.daxen.mydancekmpsharedui.features.academy.classes.viewmodel.AcademyClassesViewModel
import com.daxen.mydancekmpsharedui.features.academy.classes.viewmodel.ClassDetailViewModel
import com.daxen.mydancekmpsharedui.features.academy.classes.viewmodel.CreateClassViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val academyClassesModule = module {
    includes(dataAcademyClassesModule)
    includes(dataUserModule)
    includes(dataTeachersModule)

    viewModelOf(::AcademyClassesViewModel)
    viewModelOf(::CreateClassViewModel)
    viewModelOf(::ClassDetailViewModel)
}