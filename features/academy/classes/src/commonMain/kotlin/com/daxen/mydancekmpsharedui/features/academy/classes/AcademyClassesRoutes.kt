package com.daxen.mydancekmpsharedui.features.academy.classes

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.daxen.mydancekmpsharedui.features.academy.classes.ui.AcademyClassesScreen
import com.daxen.mydancekmpsharedui.features.academy.classes.ui.CreateClassScreen
import com.daxen.mydancekmpsharedui.features.academy.classes.viewmodel.AcademyClassesViewModel
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
            AcademyClassesScreen(viewModel = viewModel)
        }
    }
}

fun NavGraphBuilder.academyCreateClassGraph(
    appNavController: NavController
) {
    navigation<AcademyClassesDestinations.CreateClassGraph>(
        startDestination = AcademyClassesDestinations.CreateClassRoute
    ) {
        composable<AcademyClassesDestinations.CreateClassRoute> {
            val viewModel: CreateClassViewModel = koinViewModel()
            CreateClassScreen(viewModel = viewModel)
        }
    }
}