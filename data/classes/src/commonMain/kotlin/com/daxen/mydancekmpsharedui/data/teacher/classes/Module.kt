package com.daxen.mydancekmpsharedui.data.teacher.classes

import com.daxen.mydancekmpsharedui.core.firebase.coreFirebaseModule
import com.daxen.mydancekmpsharedui.data.teacher.classes.repository.TeacherClassesRepository
import com.daxen.mydancekmpsharedui.data.teacher.classes.repository.TeacherClassesRepositoryImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataTeacherClassesModule = module {
    includes(coreFirebaseModule)
    singleOf(::TeacherClassesRepositoryImpl) { bind<TeacherClassesRepository>() }
} 