package com.daxen.mydancekmpsharedui.navigation.bottomnavigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.daxen.mydancekmpsharedui.features.student.calendar.calendarNavGraph
import com.daxen.mydancekmpsharedui.features.student.reservation.reservationNavGraph
import com.daxen.mydancekmpsharedui.features.user.userNavGraph
import com.daxen.mydancekmpsharedui.navigation.CentralAppDestination

@Composable
fun BottomBarNavHost(bottomNavController: NavHostController, appNavController: NavHostController) {
    NavHost(
        navController = bottomNavController,
        startDestination = BottomBarDestination.Reservation.route
    ) {
        userNavGraph(
            appNavController = appNavController,
            navigateToLogin = {
                appNavController.navigate(CentralAppDestination.Auth.route)
            }
        )

        reservationNavGraph()
        
        calendarNavGraph()
    }
}