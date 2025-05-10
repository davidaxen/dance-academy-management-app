package com.daxen.mydancekmpsharedui.navigation.studentBottomNavigation

import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.runtime.Composable
import com.daxen.mydancekmpsharedui.features.student.calendar.CalendarGraph
import com.daxen.mydancekmpsharedui.features.student.reservation.ReservationGraph
import com.daxen.mydancekmpsharedui.features.user.UserGraph
import kotlinx.serialization.Serializable

@Serializable
sealed class StudentBottomBarDestination<T>(
    val title: String, // the title of the tab
    val selectedIcon: @Composable () -> Unit, // filled icon when selected
    val unselectedIcon: @Composable () -> Unit, // unfilled icon when not selected
    val route: T //graphs defined in feature module for each tab
){
    @Serializable
    data object User: StudentBottomBarDestination<UserGraph>(
        title = "Perfil",
        selectedIcon = {
            Icon(imageVector = Icons.Filled.AccountBox, contentDescription = "")
        },
        unselectedIcon = {
            Icon(imageVector = Icons.Outlined.AccountBox, contentDescription = "")
        },
        route = UserGraph
    )
    @Serializable
    data object Reservation: StudentBottomBarDestination<ReservationGraph>(
        title = "Reservas",
        selectedIcon = {
            Icon(imageVector = Icons.Filled.DateRange, contentDescription = "")
        },
        unselectedIcon = {
            Icon(imageVector = Icons.Outlined.DateRange, contentDescription = "")
        },
        route = ReservationGraph
    )
    @Serializable
    data object Calendar: StudentBottomBarDestination<CalendarGraph>(
        title = "Calendario",
        selectedIcon = {
            Icon(imageVector = Icons.Filled.CalendarMonth, contentDescription = "")
        },
        unselectedIcon = {
            Icon(imageVector = Icons.Outlined.CalendarMonth, contentDescription = "")
        },
        route = CalendarGraph
    )
}