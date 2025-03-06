package com.daxen.mydancekmpsharedui.domain.auth

import com.daxen.mydancekmpsharedui.data.auth.dataAuthModule
import org.koin.dsl.module

val domainAuthModule = module {
    includes(dataAuthModule)
}