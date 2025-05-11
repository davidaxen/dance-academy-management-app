package com.daxen.mydancekmpsharedui.features.academy.classes

import com.daxen.mydancekmpsharedui.data.academy.classes.dataAcademyClassesModule
import com.daxen.mydancekmpsharedui.data.user.dataUserModule
import com.daxen.mydancekmpsharedui.features.academy.classes.viewmodel.AcademyClassesViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val academyClassesModule = module {
    includes(dataAcademyClassesModule)
    includes(dataUserModule)

    viewModelOf(::AcademyClassesViewModel)
} 