package com.daxen.mydancekmpsharedui.navigation.bottomnavigation

import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.Face
import androidx.compose.runtime.Composable
import com.daxen.mydancekmpsharedui.features.auth.AuthGraph
import com.daxen.mydancekmpsharedui.features.user.UserGraph
import kotlinx.serialization.Serializable

@Serializable
sealed class BottomDestinations<T>(
    val title: String, // the title of the tab
    val selectedIcon: @Composable () -> Unit, // filled icon when selected
    val unselectedIcon: @Composable () -> Unit, // unfilled icon when not selected
    val route: T //graphs defined in feature module for each tab
){

    @Serializable
    data object User: BottomDestinations<UserGraph>(
        title = "Perfil",
        selectedIcon = {
            Icon(imageVector = Icons.Filled.AccountBox, contentDescription = "")
        },
        unselectedIcon = {
            Icon(imageVector = Icons.Outlined.AccountBox, contentDescription = "")
        },
        route = UserGraph
    )

    @Serializable
    data object Auth: BottomDestinations<AuthGraph>(
        title = "Autenticacion",
        selectedIcon = {
            Icon(imageVector = Icons.Filled.Face, contentDescription = "")
        },
        unselectedIcon = {
            Icon(imageVector = Icons.Outlined.Face, contentDescription = "")
        },
        route = AuthGraph
    )
}