package com.daxen.mydancekmpsharedui.features.academy.students

import com.daxen.mydancekmpsharedui.data.students.dataStudentsModule
import com.daxen.mydancekmpsharedui.data.user.dataUserModule
import com.daxen.mydancekmpsharedui.features.academy.students.listing.ui.StudentsListingViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val academyStudentsModule = module {
    includes(dataUserModule)
    includes(dataStudentsModule)

    singleOf(::StudentsListingViewModel)
    viewModelOf(::StudentsListingViewModel)
}