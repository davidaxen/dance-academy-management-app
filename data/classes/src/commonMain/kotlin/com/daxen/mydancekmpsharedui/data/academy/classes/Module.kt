package com.daxen.mydancekmpsharedui.data.academy.classes

import com.daxen.mydancekmpsharedui.core.firebase.coreFirebaseModule
import com.daxen.mydancekmpsharedui.data.academy.classes.repository.AcademyClassesRepository
import com.daxen.mydancekmpsharedui.data.academy.classes.repository.AcademyClassesRepositoryImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataAcademyClassesModule = module {
    includes(coreFirebaseModule)
    singleOf(::AcademyClassesRepositoryImpl) { bind<AcademyClassesRepository>() }
} 