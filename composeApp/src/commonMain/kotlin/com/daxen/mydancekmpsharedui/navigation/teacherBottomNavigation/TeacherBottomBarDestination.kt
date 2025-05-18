package com.daxen.mydancekmpsharedui.navigation.teacherBottomNavigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import com.daxen.mydancekmpsharedui.features.teacher.classes.TeacherClassesDestinations
import com.daxen.mydancekmpsharedui.features.user.UserGraph
import kotlinx.serialization.Serializable

@Serializable
sealed class TeacherBottomBarDestination<T>(
    val title: String, // the title of the tab
    val selectedIcon: @Composable () -> Unit, // filled icon when selected
    val unselectedIcon: @Composable () -> Unit, // unfilled icon when not selected
    val route: T //graphs defined in feature module for each tab
) {
    @Serializable
    data object User: TeacherBottomBarDestination<UserGraph>(
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
    data object ClassesList: TeacherBottomBarDestination<TeacherClassesDestinations.TeacherClassesListingGraph>(
        title = "Tus Clases",
        selectedIcon = {
             Icon(imageVector = Icons.Filled.Event, contentDescription = "")
        },
        unselectedIcon = {
             Icon(imageVector = Icons.Outlined.Event, contentDescription = "")
        },
        route = TeacherClassesDestinations.TeacherClassesListingGraph
    )
}