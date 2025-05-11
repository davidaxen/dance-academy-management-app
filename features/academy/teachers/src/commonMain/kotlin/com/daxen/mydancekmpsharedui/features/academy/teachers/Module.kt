package com.daxen.mydancekmpsharedui.features.academy.teachers

import com.daxen.mydancekmpsharedui.data.teachers.dataTeachersModule
import com.daxen.mydancekmpsharedui.features.academy.teachers.listing.ui.TeachersListingViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val academyTeachersModule = module {
    includes(dataTeachersModule)
    viewModelOf(::TeachersListingViewModel)
}