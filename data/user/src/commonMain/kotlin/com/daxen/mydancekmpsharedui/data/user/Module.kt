package com.daxen.mydancekmpsharedui.data.user

import com.daxen.mydancekmpsharedui.core.firebase.coreFirebaseModule
import com.daxen.mydancekmpsharedui.data.user.repository.AcademyUserRepository
import com.daxen.mydancekmpsharedui.data.user.repository.AcademyUserRepositoryImpl
import com.daxen.mydancekmpsharedui.data.user.repository.UserRepository
import com.daxen.mydancekmpsharedui.data.user.repository.UserRepositoryImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataUserModule = module {
    includes(coreFirebaseModule)
    singleOf(::UserRepositoryImpl) { bind<UserRepository>() }
    singleOf(::AcademyUserRepositoryImpl) { bind<AcademyUserRepository>() }
}