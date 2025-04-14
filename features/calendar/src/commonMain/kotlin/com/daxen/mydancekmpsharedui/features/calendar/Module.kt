package com.daxen.mydancekmpsharedui.features.calendar

import com.daxen.mydancekmpsharedui.features.calendar.ui.CalendarViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val calendarModule = module {
    viewModelOf(::CalendarViewModel)
} 