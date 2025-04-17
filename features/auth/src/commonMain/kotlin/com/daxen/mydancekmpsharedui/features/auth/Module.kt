package com.daxen.mydancekmpsharedui.features.auth

import com.daxen.mydancekmpsharedui.data.auth.dataAuthModule
import com.daxen.mydancekmpsharedui.features.auth.ui.login.LoginViewModel
import com.daxen.mydancekmpsharedui.features.auth.ui.register.RegisterViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val authModule = module {
    includes(dataAuthModule)

    viewModelOf(::LoginViewModel)
    viewModelOf(::RegisterViewModel)
}