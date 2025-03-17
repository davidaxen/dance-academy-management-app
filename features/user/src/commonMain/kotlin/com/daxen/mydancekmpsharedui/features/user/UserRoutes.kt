package com.daxen.mydancekmpsharedui.features.user

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.daxen.mydancekmpsharedui.features.user.ui.UserScreen
import kotlinx.serialization.Serializable

@Serializable
data object UserGraph

@Serializable
private data object UserScreenRoute


fun NavGraphBuilder.userNavGraph(navigateToLogin: () -> Unit) {

    navigation<UserGraph>(startDestination = UserScreenRoute) {
        composable<UserScreenRoute> {
            UserScreen(
                navigateToLogin = navigateToLogin,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}