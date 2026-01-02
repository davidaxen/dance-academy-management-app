package com.daxen.mydancekmpsharedui.data.reservation

import com.daxen.mydancekmpsharedui.core.firebase.coreFirebaseModule
import com.daxen.mydancekmpsharedui.data.reservation.repository.ReservationRepository
import com.daxen.mydancekmpsharedui.data.reservation.repository.ReservationRepositoryImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataReservationModule = module {
    includes(coreFirebaseModule)
    singleOf(::ReservationRepositoryImpl) { bind<ReservationRepository>() }
}