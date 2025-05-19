package com.daxen.mydancekmpsharedui.features.teacher.classes

import androidx.lifecycle.compose.dropUnlessResumed
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.daxen.mydancekmpsharedui.data.academy.classes.models.SpecificClassModel
import com.daxen.mydancekmpsharedui.data.academy.classes.models.WeeklyClassModel
import com.daxen.mydancekmpsharedui.features.teacher.classes.ui.ClassDetailScreen
import com.daxen.mydancekmpsharedui.features.teacher.classes.ui.TeacherClassesScreen
import com.daxen.mydancekmpsharedui.features.teacher.classes.viewmodel.ClassDetailViewModel
import com.daxen.mydancekmpsharedui.features.teacher.classes.viewmodel.TeacherClassesViewModel
import kotlinx.datetime.LocalDate
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
        val isWeekly: Boolean,
        val date: String,
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
                onClassClick = { classData, isWeekly ->
                    val classId = if (isWeekly) {
                        (classData as WeeklyClassModel).id
                    } else {
                        (classData as SpecificClassModel).id
                    }

                    val navDestination = TeacherClassesDestinations.ClassDetailRoute(
                        classId = classId,
                        isWeekly = isWeekly,
                        date = getSpanishDate(viewModel.selectedDate.value)
                    )
                    appNavController.navigate(navDestination)
                }
            )
        }
    }
}

private fun getSpanishDate(date: LocalDate): String {
    val day = date.dayOfMonth
    val month = date.monthNumber
    val year = date.year

    // Formatear la fecha en español
    return "$day/$month/$year"
}

fun NavGraphBuilder.teacherClassDetailGraph(
    onBackClick: () -> Unit
) {
    composable<TeacherClassesDestinations.ClassDetailRoute> { backStackEntry ->
        val classDetail = backStackEntry.toRoute<TeacherClassesDestinations.ClassDetailRoute>()
        val viewModel: ClassDetailViewModel = koinViewModel()
        ClassDetailScreen(
            viewModel = viewModel,
            classId = classDetail.classId,
            isWeekly = classDetail.isWeekly,
            dateSelected = classDetail.date,
            onBackClick = dropUnlessResumed {
                onBackClick()
            }
        )
    }
}