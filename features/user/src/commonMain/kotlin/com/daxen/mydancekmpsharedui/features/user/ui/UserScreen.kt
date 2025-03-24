package com.daxen.mydancekmpsharedui.features.user.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.daxen.mydancekmpsharedui.core.ui.LocalPadding
import com.daxen.mydancekmpsharedui.data.user.model.User
import com.daxen.mydancekmpsharedui.data.user.model.UserRole
import com.daxen.mydancekmpsharedui.features.user.utils.Constants
import com.daxen.mydancekmpsharedui.features.user.utils.ProfileItem
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun UserScreen(
    viewModel: UserViewModel = koinViewModel(),
    modifier: Modifier = Modifier,
    navigateToLogin: () -> Unit
) {
    val userState by viewModel.userState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(LocalPadding.current.normal),
        contentAlignment = Alignment.Center
    ) {
        when (userState) {
            is UserUiState.Loading -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Cargando usuario...")
                }
            }

            is UserUiState.Success -> {
                val user = (userState as UserUiState.Success).user
                UserProfile(user, signOut = {
                    viewModel.signOut()
                    navigateToLogin()
                })
            }

            is UserUiState.Error -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Error al cargar el usuario",
                        color = Color.Red,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { viewModel.reloadUser() }) {
                        Text("Reintentar")
                    }
                }
            }
        }
    }
}

@Composable
fun UserProfile(user: User = User(
    uid = "asdadqwdqdas",
    email = "DavidPreview@gmail.com",
    role = UserRole.STUDENT,
    name = "David Fernando Bracamonte Martins"
), signOut: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        // Header con icono de usuario y nombre (opcional)

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(vertical = 24.dp)
                .fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "User Icon",
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .padding(16.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = user.name,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = user.email,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Divider()
        Spacer(Modifier.height(16.dp))
        // Lista de opciones
        Column {
            var currentSectionTitle: String? = null
            val currentOptions = mutableListOf<ProfileItem.Option>()

            Constants.profileItems.forEachIndexed { index, item ->
                when (item) {
                    is ProfileItem.Section -> {
                        if (currentSectionTitle != null && currentOptions.isNotEmpty()) {
                            ProfileSectionCard(
                                title = currentSectionTitle!!,
                                options = currentOptions,
//                                onAction = onAction
                            )
                            currentOptions.clear()
                        }
                        currentSectionTitle = item.title
                    }

                    is ProfileItem.Option -> {
                        currentOptions.add(item)
                        if (index == Constants.profileItems.lastIndex) {
                            ProfileSectionCard(
                                title = currentSectionTitle ?: "",
                                options = currentOptions,
//                                onAction = onAction
                            )
                        }
                    }
                }
            }
        }
//        Card (
//            colors = CardDefaults.cardColors(
//                containerColor = Color.White
//            ),
//            elevation = CardDefaults.cardElevation(
//                defaultElevation = 2.dp
//            ),
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(vertical = 8.dp)
//        ) {
//            ProfileOption.entries.forEachIndexed { index, option ->
//                ProfileOptionItem(
//                    icon = option.icon,
//                    label = option.label
//                ) {
////                onOptionClick(option)
//                }
//                if (index < ProfileOption.entries.lastIndex) {
//                    Divider(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(horizontal = 8.dp),
//                        color = Color.LightGray
//                    )
//                }
//            }
//        }
//        Card (
//            colors = CardDefaults.cardColors(
//                containerColor = Color.White
//            ),
//            elevation = CardDefaults.cardElevation(
//                defaultElevation = 2.dp
//            ),
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(vertical = 8.dp)
//        ) {
//            ProfileOption2.entries.forEach { option ->
//                ProfileOptionItem(
//                    icon = option.icon,
//                    label = option.label
//                ) {
////                onOptionClick(option)
//                }
//            }
//        }
    }
}

@Composable
fun ProfileSectionCard(
    title: String,
    options: List<ProfileItem.Option>,
//    onAction: (ProfileAction) -> Unit
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 8.dp)
    )

    Card (
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 2.dp
            ),
            modifier = Modifier
                .fillMaxWidth()
        ) {
        Column {
            options.forEachIndexed { index, option ->
                ProfileOptionItem(
                    icon = option.icon,
                    label = option.label
                ) {
//                    onAction(option.action)
                }

                if (index < options.lastIndex) {
                    Divider(modifier = Modifier.padding(start = 16.dp))
                }
            }
        }
    }
}


@Composable
fun ProfileOptionItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                contentDescription = label,
                modifier = Modifier.size(24.dp)
            )

    }
}

@Composable
fun ProfileOptionItemFunciona(
    option: ProfileOption,
    onClick: () -> Unit
) {
    Card (
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .clickable { onClick() }
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row {
                Icon(
                    imageVector = option.icon,
                    contentDescription = option.label,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = option.label,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                contentDescription = option.label,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}


enum class ProfileOption(val label: String, val icon: ImageVector) {
    Orders("Mis pedidos", Icons.Default.ShoppingCart),
    MyDetails("Mis datos", Icons.Default.Info),
    AddressBook("Direcciones", Icons.Default.Home),
}
enum class ProfileOption2(val label: String, val icon: ImageVector) {
    //    PaymentMethods("Métodos de pago", Icons.Default.CreditCard),
    Notifications("Notificaciones", Icons.Default.Notifications),
    ContactPreferences("Preferencias de contacto", Icons.Default.Email),
    //    Help("Ayuda", Icons.Default.Help),
    Logout("Cerrar sesión", Icons.AutoMirrored.Filled.ExitToApp)
}
enum class ProfileOptionFunciona(val label: String, val icon: ImageVector) {
    Orders("Mis pedidos", Icons.Default.ShoppingCart),
    MyDetails("Mis datos", Icons.Default.Info),
    AddressBook("Direcciones", Icons.Default.Home),
//    PaymentMethods("Métodos de pago", Icons.Default.CreditCard),
    Notifications("Notificaciones", Icons.Default.Notifications),
    ContactPreferences("Preferencias de contacto", Icons.Default.Email),
//    Help("Ayuda", Icons.Default.Help),
    Logout("Cerrar sesión", Icons.AutoMirrored.Filled.ExitToApp)
}

@Composable
fun UserProfileAntiguo(user: User, signOut: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Hola, ${user.name}")
        Text(text = "Email: ${user.email}")

        Button(onClick = signOut) {
            Text("Desconectarse")
        }
    }
}