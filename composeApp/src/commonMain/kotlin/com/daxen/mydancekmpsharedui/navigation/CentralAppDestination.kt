package com.daxen.mydancekmpsharedui.navigation

import com.daxen.mydancekmpsharedui.features.auth.AuthGraph
import com.daxen.mydancekmpsharedui.main.MainGraph
import kotlinx.serialization.Serializable

@Serializable
sealed class CentralAppDestination<T> (
    val route: T
){
//    @Serializable
//    data object Splash : CentralDestinations<AuthGraph>(
//        route = AuthGraph
//    )


    @Serializable
    data object Auth : CentralAppDestination<AuthGraph>(
        route = AuthGraph
    )


    @Serializable
    data object Main : CentralAppDestination<MainGraph>(
        route = MainGraph
    )

}