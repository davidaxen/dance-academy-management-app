package com.daxen.mydancekmpsharedui.features.academy.classes

import androidx.lifecycle.compose.dropUnlessResumed
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.daxen.mydancekmpsharedui.data.academy.classes.models.SpecificClassModel
import com.daxen.mydancekmpsharedui.data.academy.classes.models.WeeklyClassModel
import com.daxen.mydancekmpsharedui.features.academy.classes.ui.AcademyClassesScreen
import com.daxen.mydancekmpsharedui.features.academy.classes.ui.ClassDetailScreen
import com.daxen.mydancekmpsharedui.features.academy.classes.ui.CreateClassScreen
import com.daxen.mydancekmpsharedui.features.academy.classes.ui.EditClassScreen
import com.daxen.mydancekmpsharedui.features.academy.classes.ui.StudentsReservationListScreen
import com.daxen.mydancekmpsharedui.features.academy.classes.viewmodel.AcademyClassesViewModel
import com.daxen.mydancekmpsharedui.features.academy.classes.viewmodel.ClassDetailViewModel
import com.daxen.mydancekmpsharedui.features.academy.classes.viewmodel.CreateClassViewModel
import com.daxen.mydancekmpsharedui.features.academy.classes.viewmodel.EditClassViewModel
import com.daxen.mydancekmpsharedui.features.academy.classes.viewmodel.StudentsReservationListViewModel
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel

@Serializable
sealed class AcademyClassesDestinations {
    @Serializable
    data object AcademyClassesListingGraph : AcademyClassesDestinations()

    @Serializable
    data object ClassesListingRoute : AcademyClassesDestinations()
    
    @Serializable
    data object CreateClassRoute : AcademyClassesDestinations()

    @Serializable
    data class ClassDetailRoute(
        val classId: String,
        val isWeekly: Boolean,
        val date: String,
    ) : AcademyClassesDestinations()
    
    @Serializable
    data class EditClassRoute(
        val classId: String,
        val isWeekly: Boolean
    ) : AcademyClassesDestinations()

    @Serializable
    data object CreateClassGraph : AcademyClassesDestinations()

    @Serializable
    data class StudentsReservationListRoute(
        val classId: String,
        val date: String,
        val className: String,
    ) : AcademyClassesDestinations()
}

fun NavGraphBuilder.academyClassesGraph(
    appNavController: NavController
) {
    navigation<AcademyClassesDestinations.AcademyClassesListingGraph>(
        startDestination = AcademyClassesDestinations.ClassesListingRoute
    ) {
        composable<AcademyClassesDestinations.ClassesListingRoute> {
            val viewModel: AcademyClassesViewModel = koinViewModel()
            AcademyClassesScreen(
                viewModel = viewModel,
                onClassClick = { classData, isWeekly ->
                    val classId = if (isWeekly) {
                        (classData as WeeklyClassModel).id
                    } else {
                        (classData as SpecificClassModel).id
                    }
                    
                    val navDestination = AcademyClassesDestinations.ClassDetailRoute(
                        classId = classId,
                        isWeekly = isWeekly,
                        date = viewModel.selectedDate.value.toString()
                    )
                    appNavController.navigate(navDestination)
                }
            )
        }
    }
}

fun NavGraphBuilder.academyClassDetailGraph(
    appNavController: NavController,
    onBackClick: () -> Unit
) {
    composable<AcademyClassesDestinations.ClassDetailRoute> { backStackEntry ->
        val classDetail = backStackEntry.toRoute<AcademyClassesDestinations.ClassDetailRoute>()
        val viewModel: ClassDetailViewModel = koinViewModel()
        ClassDetailScreen(
            viewModel = viewModel,
            classId = classDetail.classId,
            isWeekly = classDetail.isWeekly,
            dateSelected = classDetail.date,
            onListingReservations = { className ->
                appNavController.navigate(
                    AcademyClassesDestinations.StudentsReservationListRoute(
                        classId = classDetail.classId,
                        date = classDetail.date,
                        className = className
                    )
                )
            },
            onBackClick = dropUnlessResumed {
                onBackClick()
            },
            onDeletedSuccess = dropUnlessResumed {
                onBackClick()
            },
            onEditClick = { classId, isWeekly ->
                val navDestination = AcademyClassesDestinations.EditClassRoute(
                    classId = classId,
                    isWeekly = isWeekly
                )
                appNavController.navigate(navDestination)
            }
        )
    }
}

fun NavGraphBuilder.academyEditClassGraph(
    onBackClick: () -> Unit
) {
    composable<AcademyClassesDestinations.EditClassRoute> { backStackEntry ->
        val classEdit = backStackEntry.toRoute<AcademyClassesDestinations.EditClassRoute>()
        val viewModel: EditClassViewModel = koinViewModel()
        EditClassScreen(
            viewModel = viewModel,
            classId = classEdit.classId,
            isWeeklyClass = classEdit.isWeekly,
            onBackClick = dropUnlessResumed {
                onBackClick()
            },
            onUpdateSuccess = dropUnlessResumed {
                onBackClick()
            }
        )
    }
}

fun NavGraphBuilder.academyCreateClassGraph() {
    navigation<AcademyClassesDestinations.CreateClassGraph>(
        startDestination = AcademyClassesDestinations.CreateClassRoute
    ) {
        composable<AcademyClassesDestinations.CreateClassRoute> {
            val viewModel: CreateClassViewModel = koinViewModel()
            CreateClassScreen(viewModel = viewModel)
        }
    }
}

fun NavGraphBuilder.academyStudentsReservationListGraph(
    onBackClick: () -> Unit
) {
    composable<AcademyClassesDestinations.StudentsReservationListRoute> { backStackEntry ->
        val studentReservations = backStackEntry.toRoute<AcademyClassesDestinations.StudentsReservationListRoute>()
        val viewModel: StudentsReservationListViewModel = koinViewModel()
        StudentsReservationListScreen(
            viewModel = viewModel,
            classId = studentReservations.classId,
            date = studentReservations.date,
            className = studentReservations.className,
            onBackClick = dropUnlessResumed {
                onBackClick()
            }
        )
    }
}