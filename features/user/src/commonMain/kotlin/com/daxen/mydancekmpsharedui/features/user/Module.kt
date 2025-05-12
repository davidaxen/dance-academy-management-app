package com.daxen.mydancekmpsharedui.features.user

import com.daxen.mydancekmpsharedui.data.user.dataUserModule
import com.daxen.mydancekmpsharedui.features.user.ui.UserViewModel
import com.daxen.mydancekmpsharedui.features.user.ui.academySelection.AcademySelectionViewModel
import com.daxen.mydancekmpsharedui.features.user.ui.academyUser.AcademyUserViewModel
import com.daxen.mydancekmpsharedui.features.user.ui.sections.personalData.PersonalDataViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val userModule = module {
    includes(dataUserModule)

    viewModelOf(::UserViewModel)
    viewModelOf(::PersonalDataViewModel)
    viewModelOf(::AcademySelectionViewModel)
    viewModelOf(::AcademyUserViewModel)
}