package com.daxen.mydancekmpsharedui.features.user

import com.daxen.mydancekmpsharedui.data.user.dataUserModule
import com.daxen.mydancekmpsharedui.features.user.ui.UserViewModel
import com.daxen.mydancekmpsharedui.features.user.ui.academySelection.AcademySelectionViewModel
import com.daxen.mydancekmpsharedui.features.user.ui.academyUser.AcademyUserViewModel
import com.daxen.mydancekmpsharedui.features.user.ui.academyUser.sections.academyData.AcademyDataViewModel
import com.daxen.mydancekmpsharedui.features.user.ui.sections.personalData.PersonalDataViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val userModule = module {
    includes(dataUserModule)

    singleOf(::AcademyUserInfoProviderImpl) { bind<AcademyUserInfoProvider>() }

    viewModelOf(::UserViewModel)
    viewModelOf(::PersonalDataViewModel)
    factoryOf(::AcademySelectionViewModel)
    viewModelOf(::AcademyUserViewModel)
    viewModelOf(::AcademyDataViewModel)
}