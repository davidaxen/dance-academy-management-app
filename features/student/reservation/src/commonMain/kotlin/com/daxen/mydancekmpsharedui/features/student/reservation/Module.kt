package com.daxen.mydancekmpsharedui.features.reservation

import com.daxen.mydancekmpsharedui.data.user.dataUserModule
import com.daxen.mydancekmpsharedui.data.classes.dataClassesModule
import com.daxen.mydancekmpsharedui.data.reservation.dataReservationModule
import com.daxen.mydancekmpsharedui.features.reservation.ui.ReservationViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val reservationModule = module {
    includes(dataUserModule)
    includes(dataClassesModule)
    includes(dataReservationModule)
    viewModelOf(::ReservationViewModel)
}