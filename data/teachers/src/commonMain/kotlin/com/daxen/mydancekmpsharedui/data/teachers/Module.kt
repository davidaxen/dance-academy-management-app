package com.daxen.mydancekmpsharedui.data.teachers

import com.daxen.mydancekmpsharedui.core.firebase.coreFirebaseModule
import com.daxen.mydancekmpsharedui.data.teachers.repository.AcademyTeachersRepository
import com.daxen.mydancekmpsharedui.data.teachers.repository.AcademyTeachersRepositoryImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataTeachersModule = module {
    includes(coreFirebaseModule)
    singleOf(::AcademyTeachersRepositoryImpl) { bind<AcademyTeachersRepository>() }
}