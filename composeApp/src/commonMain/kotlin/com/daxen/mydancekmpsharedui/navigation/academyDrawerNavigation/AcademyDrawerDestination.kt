package com.daxen.mydancekmpsharedui.navigation.academyDrawerNavigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import com.daxen.mydancekmpsharedui.features.academy.students.AcademyStudentsDestinations
import com.daxen.mydancekmpsharedui.features.user.UserGraph
import kotlinx.serialization.Serializable

enum class DrawerSection(val title: String) {
    User("Usuario"),
    Students("Tus alumnos"),
}

@Serializable
sealed class AcademyDrawerDestination<T>(
    val title: String, // the title of the tab
    val selectedIcon: @Composable () -> Unit, // filled icon when selected
    val unselectedIcon: @Composable () -> Unit, // unfilled icon when not selected
    val section: DrawerSection,
    val route: T //graphs defined in feature module for each tab
){
    @Serializable
    data object User: AcademyDrawerDestination<UserGraph>(
        title = "Perfil",
        selectedIcon = {
             Icon(imageVector = Icons.Filled.AccountBox, contentDescription = "")
        },
        unselectedIcon = {
             Icon(imageVector = Icons.Outlined.AccountBox, contentDescription = "")
        },
        section = DrawerSection.User,
        route = UserGraph
    )

    @Serializable
    data object StudentsList: AcademyDrawerDestination<AcademyStudentsDestinations.AcademyStudentsGraph>(
        title = "Tus Alumnos",
        selectedIcon = {
             Icon(imageVector = Icons.Filled.AccountBox, contentDescription = "")
        },
        unselectedIcon = {
             Icon(imageVector = Icons.Outlined.AccountBox, contentDescription = "")
        },
        section = DrawerSection.Students,
        route = AcademyStudentsDestinations.AcademyStudentsGraph
    )
}