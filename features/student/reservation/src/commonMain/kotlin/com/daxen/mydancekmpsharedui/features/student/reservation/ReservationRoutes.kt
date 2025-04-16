package com.daxen.mydancekmpsharedui.features.reservation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.daxen.mydancekmpsharedui.features.reservation.ui.ReservationScreen
import kotlinx.serialization.Serializable

@Serializable
data object ReservationGraph

@Serializable
private data object ReservationScreenRoute


fun NavGraphBuilder.reservationNavGraph() {
    navigation<ReservationGraph>(startDestination = ReservationScreenRoute) {
        composable<ReservationScreenRoute> {
            ReservationScreen()
        }
    }
}