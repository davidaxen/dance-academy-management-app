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
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.daxen.mydancekmpsharedui.features.user.ui.sections.personalData.PersonalDataScreen
import com.daxen.mydancekmpsharedui.features.user.ui.UserScreen
import com.daxen.mydancekmpsharedui.features.user.ui.UserViewModel
import com.daxen.mydancekmpsharedui.features.user.ui.academySelection.AcademySelectionScreen
import com.daxen.mydancekmpsharedui.features.user.ui.academySelection.AcademySelectionViewModel
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
data object AcademySelectionGraph

@Serializable
private data object AcademySelectionRoute

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

fun NavGraphBuilder.academySelectionNavGraph(
    appNavController: NavController,
    navigateToLogin: () -> Unit
) {
    navigation<AcademySelectionGraph>(startDestination = AcademySelectionRoute) {
        composable<AcademySelectionRoute> {
            val viewModel: AcademySelectionViewModel = koinViewModel()
            Box(Modifier.fillMaxSize()) {
                AcademySelectionScreen(
                    viewModel = viewModel,
                    navigateToLogin = navigateToLogin,
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
            PersonalDataScreen(navigateBack = { appNavController.popBackStack() })
        }
    }
}