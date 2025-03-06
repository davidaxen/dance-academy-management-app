package com.daxen.mydancekmpsharedui.data.auth

import com.daxen.mydancekmpsharedui.core.firebase.coreFirebaseModule
import org.koin.dsl.module

val dataAuthModule = module {
    includes(coreFirebaseModule)
}