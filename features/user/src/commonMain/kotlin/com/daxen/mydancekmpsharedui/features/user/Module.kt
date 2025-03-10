package com.daxen.mydancekmpsharedui.features.user

import com.daxen.mydancekmpsharedui.data.user.dataUserModule
import com.daxen.mydancekmpsharedui.features.user.ui.UserViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val userModule = module {
    includes(dataUserModule)

    viewModelOf(::UserViewModel)
}