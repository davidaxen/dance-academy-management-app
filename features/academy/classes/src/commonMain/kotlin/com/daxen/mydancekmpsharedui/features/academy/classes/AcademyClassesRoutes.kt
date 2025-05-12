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
import com.daxen.mydancekmpsharedui.features.academy.classes.viewmodel.AcademyClassesViewModel
import com.daxen.mydancekmpsharedui.features.academy.classes.viewmodel.ClassDetailViewModel
import com.daxen.mydancekmpsharedui.features.academy.classes.viewmodel.CreateClassViewModel
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
        val isWeekly: Boolean
    ) : AcademyClassesDestinations()

    @Serializable
    data object CreateClassGraph : AcademyClassesDestinations()
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
                        isWeekly = isWeekly
                    )
                    appNavController.navigate(navDestination)
                }
            )
        }
    }
}

fun NavGraphBuilder.academyClassDetailGraph(
    onBackClick: () -> Unit
) {
    composable<AcademyClassesDestinations.ClassDetailRoute> { backStackEntry ->
        val classDetail = backStackEntry.toRoute<AcademyClassesDestinations.ClassDetailRoute>()
        val viewModel: ClassDetailViewModel = koinViewModel()
        ClassDetailScreen(
            viewModel = viewModel,
            classId = classDetail.classId,
            isWeekly = classDetail.isWeekly,
            onBackClick = dropUnlessResumed {
                onBackClick()
            },
            onDeletedSuccess = dropUnlessResumed {
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