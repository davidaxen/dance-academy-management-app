package com.daxen.mydancekmpsharedui.data.classes

import com.daxen.mydancekmpsharedui.core.firebase.coreFirebaseModule
import com.daxen.mydancekmpsharedui.data.classes.repository.ClassesRepository
import com.daxen.mydancekmpsharedui.data.classes.repository.ClassesRepositoryImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataClassesModule = module {
    includes(coreFirebaseModule)
    singleOf(::ClassesRepositoryImpl) { bind<ClassesRepository>() }
}