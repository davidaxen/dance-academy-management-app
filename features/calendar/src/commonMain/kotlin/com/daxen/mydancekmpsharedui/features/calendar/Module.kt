package com.daxen.mydancekmpsharedui.features.calendar

import com.daxen.mydancekmpsharedui.data.reservation.dataReservationModule
import com.daxen.mydancekmpsharedui.data.user.dataUserModule
import com.daxen.mydancekmpsharedui.features.calendar.ui.CalendarViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val calendarModule = module {
    includes(dataUserModule)
    includes(dataReservationModule)
    viewModelOf(::CalendarViewModel)
} 