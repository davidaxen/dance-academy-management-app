package com.daxen.mydancekmpsharedui

import com.daxen.mydancekmpsharedui.features.auth.authModule
import org.koin.dsl.module

val appModule = module {
    includes(
        authModule,
    )
}