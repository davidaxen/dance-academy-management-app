package com.daxen.mydancekmpsharedui.features.academy.teachers

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.daxen.mydancekmpsharedui.data.teachers.model.Teacher
import com.daxen.mydancekmpsharedui.features.academy.teachers.detail.ui.TeacherDetailScreen
import com.daxen.mydancekmpsharedui.features.academy.teachers.listing.ui.TeachersListingScreen
import com.daxen.mydancekmpsharedui.features.academy.teachers.listing.ui.TeachersListingViewModelProvider
import kotlinx.serialization.Serializable

@Serializable
sealed class AcademyTeachersDestinations {
    @Serializable
    data object AcademyTeachersGraph: AcademyTeachersDestinations()

    @Serializable
    data object TeachersListingRoute: AcademyTeachersDestinations()
    
    @Serializable
    data class TeacherDetailRoute(
        val id: String,
        val name: String,
        val lastName: String,
        val email: String,
        val birthDate: String,
        val phoneNumber: String,
        val phoneNumberPrefix: String,
        val profileImageUrl: String? = null,
        val joinedAt: String = ""
    ): AcademyTeachersDestinations()

    @Serializable
    data object InviteTeacherGraph: AcademyTeachersDestinations()

    @Serializable
    data object InviteTeacherRoute: AcademyTeachersDestinations()
}

fun NavGraphBuilder.academyTeachersGraph(
    appNavController: NavController,
) {
    navigation<AcademyTeachersDestinations.AcademyTeachersGraph>(startDestination = AcademyTeachersDestinations.TeachersListingRoute) {
        composable<AcademyTeachersDestinations.TeachersListingRoute> {
            val viewModel = TeachersListingViewModelProvider.get()
            TeachersListingScreen(
                viewModel = viewModel,
                onTeacherClick = { teacher ->
                    appNavController.navigate(
                        AcademyTeachersDestinations.TeacherDetailRoute(
                            id = teacher.uid,
                            name = teacher.name,
                            lastName = teacher.lastName,
                            email = teacher.email,
                            birthDate = teacher.birthDate,
                            phoneNumber = teacher.phoneNumber,
                            phoneNumberPrefix = teacher.phoneNumberPrefix,
                            profileImageUrl = teacher.profileImageUrl,
                            joinedAt = teacher.joinedAt
                        )
                    )
                }
            )
        }
    }
}

fun NavGraphBuilder.academyInvitationTeachersGraph() {
    navigation<AcademyTeachersDestinations.InviteTeacherGraph>(
        startDestination = AcademyTeachersDestinations.InviteTeacherRoute
    ) {
        composable<AcademyTeachersDestinations.InviteTeacherRoute> {
            // Aquí se implementará la pantalla de invitación de profesores
        }
    }
}

fun NavGraphBuilder.academyTeachersSectionsGraph(
    onBackClick: () -> Unit = {},
) {
    composable<AcademyTeachersDestinations.TeacherDetailRoute>(
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
        val teacherDetail = backStackEntry.toRoute<AcademyTeachersDestinations.TeacherDetailRoute>()
        val teacher = Teacher(
            uid = teacherDetail.id,
            name = teacherDetail.name,
            lastName = teacherDetail.lastName,
            email = teacherDetail.email,
            birthDate = teacherDetail.birthDate,
            phoneNumber = teacherDetail.phoneNumber,
            phoneNumberPrefix = teacherDetail.phoneNumberPrefix,
            profileImageUrl = teacherDetail.profileImageUrl,
            joinedAt = teacherDetail.joinedAt
        )
        TeacherDetailScreen(
            teacher = teacher,
            onBackClick = onBackClick
        )
    }
} 