package com.daxen.mydancekmpsharedui

import com.daxen.mydancekmpsharedui.features.auth.authModule
import com.daxen.mydancekmpsharedui.features.user.userModule
import org.koin.dsl.module

val appModule = module {
    includes(
        authModule,
        userModule
    )
}