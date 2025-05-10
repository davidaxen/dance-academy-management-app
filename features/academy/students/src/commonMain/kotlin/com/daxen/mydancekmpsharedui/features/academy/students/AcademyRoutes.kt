package com.daxen.mydancekmpsharedui.features.academy.students

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.daxen.mydancekmpsharedui.features.academy.students.listing.ui.StudentsListingScreen
import com.daxen.mydancekmpsharedui.features.academy.students.listing.ui.StudentsListingViewModel
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel

@Serializable
sealed class AcademyStudentsDestinations {
    @Serializable
    data object AcademyStudentsGraph: AcademyStudentsDestinations()

    @Serializable
    data object StudentsListingRoute: AcademyStudentsDestinations()
}

fun NavGraphBuilder.academyStudentsGraph(
) {
    navigation<AcademyStudentsDestinations.AcademyStudentsGraph>(startDestination = AcademyStudentsDestinations.StudentsListingRoute) {
        composable<AcademyStudentsDestinations.StudentsListingRoute> {
            val viewModel: StudentsListingViewModel = koinViewModel()
            StudentsListingScreen(
                viewModel = viewModel
            )
        }
    }
}