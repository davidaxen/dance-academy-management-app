package com.daxen.mydancekmpsharedui.features.academy.students

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.daxen.mydancekmpsharedui.data.students.model.DanceRole
import com.daxen.mydancekmpsharedui.data.students.model.Student
import com.daxen.mydancekmpsharedui.features.academy.students.detail.ui.StudentDetailScreen
import com.daxen.mydancekmpsharedui.features.academy.students.listing.ui.StudentsListingScreen
import com.daxen.mydancekmpsharedui.features.academy.students.listing.ui.StudentsListingViewModelProvider
import kotlinx.serialization.Serializable

@Serializable
sealed class AcademyStudentsDestinations {
    @Serializable
    data object AcademyStudentsGraph: AcademyStudentsDestinations()

    @Serializable
    data object StudentsListingRoute: AcademyStudentsDestinations()
    
    @Serializable
    data class StudentDetailRoute(
        val id: String,
        val name: String,
        val lastName: String,
        val email: String,
        val birthDate: String,
        val phoneNumber: String,
        val phoneNumberPrefix: String,
        val danceRole: DanceRole,
        val profileImageUrl: String? = null
    ): AcademyStudentsDestinations()
}

fun NavGraphBuilder.academyStudentsGraph(
    appNavController: NavController,
) {
    navigation<AcademyStudentsDestinations.AcademyStudentsGraph>(startDestination = AcademyStudentsDestinations.StudentsListingRoute) {
        composable<AcademyStudentsDestinations.StudentsListingRoute> {
            val viewModel = StudentsListingViewModelProvider.get()
            StudentsListingScreen(
                viewModel = viewModel,
                onStudentClick = { student ->
                    appNavController.navigate(
                        AcademyStudentsDestinations.StudentDetailRoute(
                            id = student.uid,
                            name = student.name,
                            lastName = student.lastName,
                            email = student.email,
                            birthDate = student.birthDate,
                            phoneNumber = student.phoneNumber,
                            phoneNumberPrefix = student.phoneNumberPrefix,
                            danceRole = student.danceRole,
                            profileImageUrl = student.profileImageUrl
                        )
                    )
                }
            )
        }
    }
}

fun NavGraphBuilder.studentDetailGraph(
    onBackClick: () -> Unit = {},
) {
    composable<AcademyStudentsDestinations.StudentDetailRoute>(
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(500)
            )
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(500)
            )
        }
    ) { backStackEntry ->
        val studentDetail = backStackEntry.toRoute<AcademyStudentsDestinations.StudentDetailRoute>()
        val student = Student(
            uid = studentDetail.id,
            name = studentDetail.name,
            lastName = studentDetail.lastName,
            email = studentDetail.email,
            birthDate = studentDetail.birthDate,
            phoneNumber = studentDetail.phoneNumber,
            phoneNumberPrefix = studentDetail.phoneNumberPrefix,
            danceRole = studentDetail.danceRole,
            profileImageUrl = studentDetail.profileImageUrl
        )
        StudentDetailScreen(
            student = student,
            onBackClick = onBackClick
        )
    }
}