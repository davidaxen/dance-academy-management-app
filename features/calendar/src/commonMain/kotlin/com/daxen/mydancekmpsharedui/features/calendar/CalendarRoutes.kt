package com.daxen.mydancekmpsharedui.features.calendar

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.daxen.mydancekmpsharedui.features.calendar.ui.CalendarScreen
import kotlinx.serialization.Serializable

@Serializable
data object CalendarGraph

@Serializable
private data object CalendarScreenRoute

fun NavGraphBuilder.calendarNavGraph() {
    navigation<CalendarGraph>(startDestination = CalendarScreenRoute) {
        composable<CalendarScreenRoute> {
            CalendarScreen()
        }
    }
} 