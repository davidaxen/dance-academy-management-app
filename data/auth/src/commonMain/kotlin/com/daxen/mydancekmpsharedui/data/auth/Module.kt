package com.daxen.mydancekmpsharedui.data.auth

import com.daxen.mydancekmpsharedui.core.firebase.coreFirebaseModule
import com.daxen.mydancekmpsharedui.data.auth.repository.AuthRepository
import com.daxen.mydancekmpsharedui.data.auth.repository.AuthRepositoryImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataAuthModule = module {
    includes(coreFirebaseModule)
    singleOf(::AuthRepositoryImpl) { bind<AuthRepository>() }
}