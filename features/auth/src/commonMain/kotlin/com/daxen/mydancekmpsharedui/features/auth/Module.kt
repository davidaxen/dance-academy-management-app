package com.daxen.mydancekmpsharedui.features.auth

import com.daxen.mydancekmpsharedui.data.auth.dataAuthModule
import com.daxen.mydancekmpsharedui.data.user.dataUserModule
import com.daxen.mydancekmpsharedui.features.auth.academy.ui.academyInfo.AcademyInfoViewModel
import com.daxen.mydancekmpsharedui.features.auth.academy.ui.logoUploader.LogoUploaderViewModel
import com.daxen.mydancekmpsharedui.features.auth.ui.login.LoginViewModel
import com.daxen.mydancekmpsharedui.features.auth.ui.personalInfo.PersonalInfoViewModel
import com.daxen.mydancekmpsharedui.features.auth.ui.register.RegisterViewModel
import com.daxen.mydancekmpsharedui.features.auth.ui.danceRoleSelection.DanceRoleSelectionViewModel
import com.daxen.mydancekmpsharedui.features.auth.ui.userRoleSelection.UserRoleSelectionViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val authModule = module {
    includes(dataAuthModule)
    includes(dataUserModule)

    viewModelOf(::AuthViewModel)

    viewModelOf(::LoginViewModel)
    viewModelOf(::RegisterViewModel)

    viewModelOf(::UserRoleSelectionViewModel)
    viewModelOf(::PersonalInfoViewModel)
    viewModelOf(::DanceRoleSelectionViewModel)

    viewModelOf(::AcademyInfoViewModel)
    viewModelOf(::LogoUploaderViewModel)
}