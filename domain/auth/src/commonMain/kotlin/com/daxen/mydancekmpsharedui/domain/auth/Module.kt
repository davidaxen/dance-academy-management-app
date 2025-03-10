package com.daxen.mydancekmpsharedui.domain.auth

import com.daxen.mydancekmpsharedui.data.auth.dataAuthModule
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val domainAuthModule = module {
    includes(dataAuthModule)

    factoryOf(::LogOutUseCase)
    factoryOf(::LoginUseCase)
    factoryOf(::RegisterUseCase)
}