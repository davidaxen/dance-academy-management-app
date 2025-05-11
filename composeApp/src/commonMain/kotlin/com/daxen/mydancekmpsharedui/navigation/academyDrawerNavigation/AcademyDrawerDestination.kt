package com.daxen.mydancekmpsharedui.navigation.academyDrawerNavigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import com.daxen.mydancekmpsharedui.features.academy.classes.AcademyClassesDestinations
import com.daxen.mydancekmpsharedui.features.academy.students.AcademyStudentsDestinations
import com.daxen.mydancekmpsharedui.features.academy.teachers.AcademyTeachersDestinations
import com.daxen.mydancekmpsharedui.features.user.UserGraph
import kotlinx.serialization.Serializable

enum class DrawerSection(val title: String) {
    User("Usuario"),
    Students("Alumnos"),
    Teachers("Profesores"),
    Classes("Clases"),
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
    data object ClassesList: AcademyDrawerDestination<AcademyClassesDestinations.AcademyClassesListingGraph>(
        title = "Tus Clases",
        selectedIcon = {
            Icon(imageVector = Icons.Filled.Event, contentDescription = "")
        },
        unselectedIcon = {
            Icon(imageVector = Icons.Outlined.Event, contentDescription = "")
        },
        section = DrawerSection.Classes,
        route = AcademyClassesDestinations.AcademyClassesListingGraph
    )

    @Serializable
    data object ClassesCreation: AcademyDrawerDestination<AcademyClassesDestinations.CreateClassRoute>(
        title = "Invitaciones Profesores",
        selectedIcon = {
            Icon(imageVector = Icons.Filled.Add, contentDescription = "")
        },
        unselectedIcon = {
            Icon(imageVector = Icons.Outlined.Add, contentDescription = "")
        },
        section = DrawerSection.Classes,
        route = AcademyClassesDestinations.CreateClassRoute
    )

    @Serializable
    data object StudentsList: AcademyDrawerDestination<AcademyStudentsDestinations.AcademyStudentsGraph>(
        title = "Tus Alumnos",
        selectedIcon = {
             Icon(imageVector = Icons.Filled.Groups, contentDescription = "")
        },
        unselectedIcon = {
             Icon(imageVector = Icons.Outlined.Groups, contentDescription = "")
        },
        section = DrawerSection.Students,
        route = AcademyStudentsDestinations.AcademyStudentsGraph
    )

    @Serializable
    data object TeachersList: AcademyDrawerDestination<AcademyTeachersDestinations.AcademyTeachersGraph>(
        title = "Tus Profesores",
        selectedIcon = {
            Icon(imageVector = Icons.Filled.People, contentDescription = "")
        },
        unselectedIcon = {
            Icon(imageVector = Icons.Outlined.People, contentDescription = "")
        },
        section = DrawerSection.Teachers,
        route = AcademyTeachersDestinations.AcademyTeachersGraph
    )

    @Serializable
    data object StudentsInvitation: AcademyDrawerDestination<AcademyStudentsDestinations.InviteStudentGraph>(
        title = "Invitaciones Alumnos",
        selectedIcon = {
             Icon(imageVector = Icons.Filled.PersonAdd, contentDescription = "")
        },
        unselectedIcon = {
             Icon(imageVector = Icons.Outlined.PersonAdd, contentDescription = "")
        },
        section = DrawerSection.Students,
        route = AcademyStudentsDestinations.InviteStudentGraph
    )

    @Serializable
    data object TeachersInvitation: AcademyDrawerDestination<AcademyTeachersDestinations.InviteTeacherGraph>(
        title = "Invitaciones Profesores",
        selectedIcon = {
            Icon(imageVector = Icons.Filled.PersonAdd, contentDescription = "")
        },
        unselectedIcon = {
            Icon(imageVector = Icons.Outlined.PersonAdd, contentDescription = "")
        },
        section = DrawerSection.Teachers,
        route = AcademyTeachersDestinations.InviteTeacherGraph
    )
}