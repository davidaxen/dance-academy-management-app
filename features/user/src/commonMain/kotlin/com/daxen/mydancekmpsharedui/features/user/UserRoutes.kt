package com.daxen.mydancekmpsharedui.features.user

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.dropUnlessStarted
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.daxen.mydancekmpsharedui.data.user.model.UserRole
import com.daxen.mydancekmpsharedui.features.user.ui.sections.personalData.PersonalDataScreen
import com.daxen.mydancekmpsharedui.features.user.ui.UserScreen
import com.daxen.mydancekmpsharedui.features.user.ui.UserViewModel
import com.daxen.mydancekmpsharedui.features.user.ui.academySelection.AcademySelectionScreen
import com.daxen.mydancekmpsharedui.features.user.ui.academySelection.AcademySelectionViewModel
import com.daxen.mydancekmpsharedui.features.user.ui.academyUser.AcademyUserScreen
import com.daxen.mydancekmpsharedui.features.user.ui.academyUser.AcademyUserViewModel
import com.daxen.mydancekmpsharedui.features.user.ui.academyUser.sections.academyData.AcademyDataScreen
import com.daxen.mydancekmpsharedui.features.user.ui.components.UserDataSummary
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel

@Serializable
data object UserGraph

@Serializable
private data object UserScreenRoute

@Serializable
data object UserOptionsGraph

@Serializable
sealed class ProfileAction {
    @Serializable
    data object PersonalInfoRoute : ProfileAction()
    @Serializable
    data object LogOut : ProfileAction()
    @Serializable
    data object DeleteAccount : ProfileAction()
}

@Serializable
sealed class AcademyProfileAction {
    @Serializable
    data object AcademyInfoRoute : AcademyProfileAction()
    @Serializable
    data object ManageTeachersRoute : AcademyProfileAction()
    @Serializable
    data object ManageStudentsRoute : AcademyProfileAction()
    @Serializable
    data object ManageScheduleRoute : AcademyProfileAction()
    @Serializable
    data object CustomizationRoute : AcademyProfileAction()
    @Serializable
    data object LogOut : AcademyProfileAction()
    @Serializable
    data object DeleteAccount : AcademyProfileAction()
}

@Serializable
data object AcademySelectionGraph

@Serializable
private data object AcademySelectionRoute

@Serializable
data object AcademyUserGraph

@Serializable
private data object AcademyUserScreenRoute

@Serializable
data object AcademyOptionsGraph

fun NavGraphBuilder.userNavGraph(navigateToLogin: () -> Unit, appNavController: NavController) {
    navigation<UserGraph>(startDestination = UserScreenRoute) {
        composable<UserScreenRoute> {
            val viewModel: UserViewModel = koinViewModel()
            UserScreen(
                viewModel = viewModel,
                showTopSection = true,
                navigateToLogin = navigateToLogin,
                navigateToSection = { action ->
                    when (action) {
                        is ProfileAction.PersonalInfoRoute -> {
                            appNavController.navigate(ProfileAction.PersonalInfoRoute)
                        }
                        is ProfileAction.LogOut -> {
                            viewModel.signOut()
                            navigateToLogin()
                        }
                        is ProfileAction.DeleteAccount -> {

                        }
                    }
                }
            )
        }
    }
}

fun NavGraphBuilder.academyUserNavGraph(navigateToLogin: () -> Unit, appNavController: NavController) {
    navigation<AcademyUserGraph>(startDestination = AcademyUserScreenRoute) {
        composable<AcademyUserScreenRoute> {
            val viewModel: AcademyUserViewModel = koinViewModel()
            AcademyUserScreen(
                viewModel = viewModel,
                navigateToSection = { action ->
                    when (action) {
                        is AcademyProfileAction.AcademyInfoRoute -> {
                            appNavController.navigate(AcademyProfileAction.AcademyInfoRoute)
                        }
                        is AcademyProfileAction.ManageTeachersRoute -> {
                            // Navegación futura a pantalla de gestión de profesores
                        }
                        is AcademyProfileAction.ManageStudentsRoute -> {
                            // Navegación futura a pantalla de gestión de estudiantes
                        }
                        is AcademyProfileAction.ManageScheduleRoute -> {
                            // Navegación futura a pantalla de gestión de horarios
                        }
                        is AcademyProfileAction.CustomizationRoute -> {
                            // Navegación futura a pantalla de personalización
                        }
                        is AcademyProfileAction.LogOut -> {
                            viewModel.signOut()
                            navigateToLogin()
                        }
                        is AcademyProfileAction.DeleteAccount -> {
                            // Lógica para borrar cuenta
                        }
                    }
                }
            )
        }
    }
}

fun NavGraphBuilder.academySelectionNavGraph(
    appNavController: NavController,
    navigateToStudentAcademy: () -> Unit,
    navigateToTeacherAcademy: () -> Unit,
    navigateToLogin: () -> Unit
) {
    navigation<AcademySelectionGraph>(startDestination = AcademySelectionRoute) {
        composable<AcademySelectionRoute> {
            val viewModel: AcademySelectionViewModel = koinViewModel()
            Box(Modifier.fillMaxSize()) {
                AcademySelectionScreen(
                    viewModel = viewModel,
                    navigateToLogin = navigateToLogin,
                    navigateToAcademy = { userRole ->
                        when (userRole) {
                            UserRole.STUDENT -> navigateToStudentAcademy()
                            UserRole.TEACHER -> navigateToTeacherAcademy()
                            else -> {}
                        }
                    },
                    modifier = Modifier.align(Alignment.TopCenter),
                    navigateToSection = { action ->
                        when (action) {
                            is ProfileAction.PersonalInfoRoute -> {
                                appNavController.navigate(ProfileAction.PersonalInfoRoute)
                            }
                            is ProfileAction.LogOut -> {
                                viewModel.signOut()
                                navigateToLogin()
                            }
                            is ProfileAction.DeleteAccount -> {

                            }
                        }
                    }
                )
                Column(modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter)) {
                    HorizontalDivider()
                    UserDataSummary(viewModel.currentUser.value) {
                        viewModel.signOut()
                        navigateToLogin()
                    }
                }
            }
        }
    }
}

fun NavGraphBuilder.userOptionsNavGraph(appNavController: NavController) {
    navigation<UserOptionsGraph>(startDestination = ProfileAction.PersonalInfoRoute) {
        composable<ProfileAction.PersonalInfoRoute>(
            enterTransition = {
                slideIntoContainer(
                    animationSpec = tween(300),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                ) + fadeIn(animationSpec = tween(300, easing = LinearEasing))
            },
            exitTransition = {
                slideOutOfContainer(
                    animationSpec = tween(300),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                ) + fadeOut(animationSpec = tween(300, easing = EaseIn))
            }
        ) {
            PersonalDataScreen(navigateBack = dropUnlessStarted { appNavController.popBackStack() })
        }
    }
}

fun NavGraphBuilder.academyOptionsNavGraph(appNavController: NavController) {
    navigation<AcademyOptionsGraph>(startDestination = AcademyProfileAction.AcademyInfoRoute) {
        composable<AcademyProfileAction.AcademyInfoRoute>(
            enterTransition = {
                slideIntoContainer(
                    animationSpec = tween(300),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                ) + fadeIn(animationSpec = tween(300, easing = LinearEasing))
            },
            exitTransition = {
                slideOutOfContainer(
                    animationSpec = tween(300),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                ) + fadeOut(animationSpec = tween(300, easing = EaseIn))
            }
        ) {
            AcademyDataScreen(navigateBack = dropUnlessStarted { appNavController.popBackStack() })
        }
    }
}