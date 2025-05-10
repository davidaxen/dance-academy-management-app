package com.daxen.mydancekmpsharedui.navigation

import com.daxen.mydancekmpsharedui.features.auth.AuthGraph
import kotlinx.serialization.Serializable

@Serializable
sealed class CentralAppDestination<T> (
    val route: T
){
    @Serializable
    data object Auth : CentralAppDestination<AuthGraph>(
        route = AuthGraph
    )
}