package com.daxen.mydancekmpsharedui.features.academy.students

import com.daxen.mydancekmpsharedui.features.academy.students.listing.ui.StudentsListingViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val academyStudentsModule = module {
//    includes(dataUserModule)
//    includes(dataReservationModule)
    viewModelOf(::StudentsListingViewModel)
    single { StudentsListingViewModel() }
}