package com.daxen.mydancekmpsharedui.features.academy.classes

import com.daxen.mydancekmpsharedui.data.academy.classes.dataAcademyClassesModule
import com.daxen.mydancekmpsharedui.features.academy.classes.viewmodel.AcademyClassesViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val featuresAcademyClassesModule = module {
    includes(dataAcademyClassesModule)
    factory { (academyId: String) -> AcademyClassesViewModel(get(), academyId) }
} 