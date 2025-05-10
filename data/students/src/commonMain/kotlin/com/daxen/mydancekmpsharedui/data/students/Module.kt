package com.daxen.mydancekmpsharedui.data.students

import com.daxen.mydancekmpsharedui.core.firebase.coreFirebaseModule
import com.daxen.mydancekmpsharedui.data.students.repository.AcademyStudentsRepository
import com.daxen.mydancekmpsharedui.data.students.repository.AcademyStudentsRepositoryImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataStudentsModule = module {
    includes(coreFirebaseModule)
    singleOf(::AcademyStudentsRepositoryImpl) { bind<AcademyStudentsRepository>() }
}