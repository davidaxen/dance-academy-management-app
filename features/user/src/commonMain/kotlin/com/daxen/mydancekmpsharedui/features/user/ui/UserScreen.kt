package com.daxen.mydancekmpsharedui.features.user.ui

import androidx.compose.foundation.border
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
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
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
import com.daxen.mydancekmpsharedui.data.user.model.User
import com.daxen.mydancekmpsharedui.data.user.model.UserRole
import com.daxen.mydancekmpsharedui.features.user.ProfileAction
import com.daxen.mydancekmpsharedui.features.user.utils.Constants
import com.daxen.mydancekmpsharedui.features.user.utils.ProfileItem
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun UserScreen(
    viewModel: UserViewModel = koinViewModel(),
    modifier: Modifier = Modifier,
    navigateToLogin: () -> Unit,
    navigateToSection: (ProfileAction) -> Unit
) {
    val userState by viewModel.userState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize(),
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
                UserProfile(
                    user, signOut = {
                        viewModel.signOut()
                        navigateToLogin()
                    },
                    navigateToSection = navigateToSection
                )
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
fun UserProfile(
    user: User = User(
        uid = "asdadqwdqdas",
        email = "DavidPreview@gmail.com",
        role = UserRole.STUDENT,
        name = "David Fernando Bracamonte Martins"
    ),
    signOut: () -> Unit,
    navigateToSection: (ProfileAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Header con icono de usuario y nombre (opcional)
        Surface(
            elevation = 4.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(vertical = 24.dp, horizontal = 16.dp)
                    .fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "User Icon",
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.Gray, CircleShape)
                        .padding(16.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text(
                        text = user.name,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = user.email,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))
        // Lista de opciones
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 8.dp)
                .weight(2f)
                .verticalScroll(rememberScrollState())
        ) {
            var currentSectionTitle: String? = null
            val currentOptions = mutableListOf<ProfileItem.Option>()

            Constants.profileItems.forEachIndexed { index, item ->
                when (item) {
                    is ProfileItem.Section -> {
                        if (currentSectionTitle != null && currentOptions.isNotEmpty()) {
                            ProfileSectionCard(
                                title = currentSectionTitle!!,
                                options = currentOptions,
                                navigateToSection = navigateToSection,
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
                                navigateToSection = navigateToSection,
                            )
                        }
                    }
                }
            }
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            TextButton({}, modifier = Modifier.padding(bottom = 4.dp)) {
                Text("BORRAR CUENTA", color = Color.Red)
            }
//            Button({},
//                modifier = Modifier.padding(bottom = 4.dp).fillMaxWidth(),
//                colors = ButtonDefaults.buttonColors(
//                    backgroundColor = Color.Red
//                )
//            ) {
//                Text("BORRAR CUENTA", color = Color.White, modifier = Modifier.padding(4.dp))
//            }
        }
    }
}

@Composable
fun ProfileSectionCard(
    title: String,
    options: List<ProfileItem.Option>,
    navigateToSection: (ProfileAction) -> Unit,
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 8.dp)
    )
    Card(
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
                    label = option.label,
                    color = option.color
                ) {
                    option.onClick?.let { navigateToSection(it) }
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
    color: Color,
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
                tint = color,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = color
            )
        }
        Icon(
            imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
            contentDescription = label,
            tint = color,
            modifier = Modifier.size(24.dp)
        )
    }
}