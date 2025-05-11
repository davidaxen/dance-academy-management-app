package com.daxen.mydancekmpsharedui.features.academy.classes

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.daxen.mydancekmpsharedui.features.academy.classes.ui.AcademyClassesScreen
import com.daxen.mydancekmpsharedui.features.academy.classes.viewmodel.AcademyClassesViewModel
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel

@Serializable
sealed class AcademyClassesDestinations {
    @Serializable
    data object AcademyClassesGraph : AcademyClassesDestinations()

    @Serializable
    data object ClassesListingRoute : AcademyClassesDestinations()
}

fun NavGraphBuilder.academyClassesGraph(
    appNavController: NavController
) {
    navigation<AcademyClassesDestinations.AcademyClassesGraph>(
        startDestination = AcademyClassesDestinations.ClassesListingRoute
    ) {
        composable<AcademyClassesDestinations.ClassesListingRoute> {
            val viewModel: AcademyClassesViewModel = koinViewModel()
            AcademyClassesScreen(viewModel = viewModel)
        }
    }
} 