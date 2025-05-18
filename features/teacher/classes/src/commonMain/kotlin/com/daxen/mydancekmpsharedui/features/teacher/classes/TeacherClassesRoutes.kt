package com.daxen.mydancekmpsharedui.features.teacher.classes

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.daxen.mydancekmpsharedui.features.teacher.classes.ui.TeacherClassesScreen
import com.daxen.mydancekmpsharedui.features.teacher.classes.viewmodel.TeacherClassesViewModel
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel

@Serializable
sealed class TeacherClassesDestinations {
    @Serializable
    data object TeacherClassesListingGraph : TeacherClassesDestinations()

    @Serializable
    data object ClassesListingRoute : TeacherClassesDestinations()

    @Serializable
    data class ClassDetailRoute(
        val classId: String,
        val isWeekly: Boolean
    ) : TeacherClassesDestinations()
}

fun NavGraphBuilder.teacherClassesGraph(
    appNavController: NavController
) {
    navigation<TeacherClassesDestinations.TeacherClassesListingGraph>(
        startDestination = TeacherClassesDestinations.ClassesListingRoute
    ) {
        composable<TeacherClassesDestinations.ClassesListingRoute> {
            val viewModel: TeacherClassesViewModel = koinViewModel()
            TeacherClassesScreen(
                viewModel = viewModel,
//                onClassClick = { classData, isWeekly ->
//                    val classId = if (isWeekly) {
//                        (classData as WeeklyClassModel).id
//                    } else {
//                        (classData as SpecificClassModel).id
//                    }
//
//                    val navDestination = TeacherClassesDestinations.ClassDetailRoute(
//                        classId = classId,
//                        isWeekly = isWeekly
//                    )
//                    appNavController.navigate(navDestination)
//                }
            )
        }
    }
}