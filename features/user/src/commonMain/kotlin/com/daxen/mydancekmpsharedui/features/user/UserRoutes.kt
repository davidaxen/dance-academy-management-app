package com.daxen.mydancekmpsharedui.features.user

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.daxen.mydancekmpsharedui.features.user.ui.UserScreen
import com.daxen.mydancekmpsharedui.features.user.ui.UserViewModel
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel

@Serializable
data object UserGraph

@Serializable
data object UserScreenRoute


fun NavGraphBuilder.userNavGraph(
) {
//    composable<User> {
//        val viewModel: UserViewModel = koinViewModel()
//        UserScreen(
//            viewModel = viewModel,
//            modifier = Modifier.fillMaxSize()
//        )
//    }

    navigation<UserGraph>(startDestination = UserScreenRoute) {
        composable<UserScreenRoute> {
            val viewModel: UserViewModel = koinViewModel()
            UserScreen(
                viewModel = viewModel,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}