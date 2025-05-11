package com.daxen.mydancekmpsharedui.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.daxen.mydancekmpsharedui.core.ui.LocalPadding
import com.daxen.mydancekmpsharedui.features.academy.students.AcademyStudentsDestinations
import com.daxen.mydancekmpsharedui.features.academy.students.invitations.invite.InvitationStudentViewModelProvider
import com.daxen.mydancekmpsharedui.features.academy.students.listing.ui.StudentsListingViewModelProvider
import com.daxen.mydancekmpsharedui.features.academy.teachers.AcademyTeachersDestinations
import com.daxen.mydancekmpsharedui.features.academy.teachers.invitations.invite.InvitationTeacherViewModelProvider
import com.daxen.mydancekmpsharedui.features.academy.teachers.listing.ui.TeachersListingViewModelProvider
import com.daxen.mydancekmpsharedui.features.user.UserGraph
import com.daxen.mydancekmpsharedui.navigation.academyDrawerNavigation.AcademyDrawerDestination
import com.daxen.mydancekmpsharedui.navigation.academyDrawerNavigation.AcademyDrawerNavHost
import kotlinx.coroutines.launch

// Clase de ayuda para configurar qué elementos mostrar en la TopBar según la pantalla
data class TopBarConfig(
    val showSearch: Boolean = false,
    val showFilter: Boolean = false
)

@Composable
fun AcademyMainScreen(appNavController: NavHostController) {
    val drawerNavController = rememberNavController()
    val drawerScreens = remember {
        listOf(
            AcademyDrawerDestination.User,
            AcademyDrawerDestination.StudentsList,
            AcademyDrawerDestination.StudentsInvitation,
            AcademyDrawerDestination.TeachersList,
            AcademyDrawerDestination.TeachersInvitation,
            AcademyDrawerDestination.ClassesList,
        )
    }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    
    // Obtener los ViewModels compartidos
    val studentsListingViewModel = StudentsListingViewModelProvider.get()
    val invitationStudentViewModel = InvitationStudentViewModelProvider.get()
    val teachersListingViewModel = TeachersListingViewModelProvider.get()
    val invitationTeacherViewModel = InvitationTeacherViewModelProvider.get()
    
    // Obtener la query actual del ViewModel según la pantalla
    val navBackStackEntry by drawerNavController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    
    // Determinar en qué pantalla estamos
    val isStudentsScreen = currentDestination?.hierarchy?.any { 
        it.hasRoute(AcademyStudentsDestinations.AcademyStudentsGraph::class)
    } == true
    
    val isStudentsInvitationsScreen = currentDestination?.hierarchy?.any { 
        it.hasRoute(AcademyStudentsDestinations.InviteStudentGraph::class)
    } == true
    
    val isTeachersScreen = currentDestination?.hierarchy?.any { 
        it.hasRoute(AcademyTeachersDestinations.AcademyTeachersGraph::class)
    } == true
    
    val isTeachersInvitationsScreen = currentDestination?.hierarchy?.any { 
        it.hasRoute(AcademyTeachersDestinations.InviteTeacherGraph::class)
    } == true
    
    // Configurar el título y los botones según la pantalla actual
    val (screenTitle, topBarConfig) = getScreenInfo(currentDestination, drawerScreens)
    
    // Determinar si estamos en una pantalla con búsqueda
    val isActiveSearchScreen = isStudentsScreen || isStudentsInvitationsScreen || 
                              isTeachersScreen || isTeachersInvitationsScreen
    
    // Obtener la consulta de búsqueda según la pantalla activa
    val currentSearchQuery by when {
        isStudentsInvitationsScreen -> invitationStudentViewModel.searchQuery.collectAsState()
        isTeachersScreen -> teachersListingViewModel.searchQuery.collectAsState()
        isTeachersInvitationsScreen -> invitationTeacherViewModel.searchQuery.collectAsState()
        else -> studentsListingViewModel.searchQuery.collectAsState() // Por defecto o si es isStudentsScreen
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerContent(drawerScreens, currentDestination, drawerNavController) {
                scope.launch {
                    if (drawerState.isOpen) {
                        drawerState.close()
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopBar(
                    title = screenTitle,
                    showSearch = topBarConfig.showSearch,
                    showFilter = topBarConfig.showFilter,
                    initialSearchQuery = currentSearchQuery,
                    isActiveSearchScreen = isActiveSearchScreen,
                    onSearchQuery = { query ->
                        // Dirigir la búsqueda al ViewModel correcto según la pantalla
                        when {
                            isStudentsScreen -> studentsListingViewModel.onSearchQueryChanged(query)
                            isStudentsInvitationsScreen -> invitationStudentViewModel.onSearchQueryChanged(query)
                            isTeachersScreen -> teachersListingViewModel.onSearchQueryChanged(query)
                            isTeachersInvitationsScreen -> invitationTeacherViewModel.onSearchQueryChanged(query)
                        }
                    },
                    onFilterClick = {
                        // Por ahora no hace nada
                    }
                ) {
                    scope.launch {
                        if (drawerState.isClosed) {
                            drawerState.open()
                        } else {
                            drawerState.close()
                        }
                    }
                }
            }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                AcademyDrawerNavHost(
                    drawerNavController = drawerNavController,
                    appNavController = appNavController
                )
            }
        }
    }
}

// Función que determina el título y la configuración de la barra superior según la pantalla
@Composable
private fun getScreenInfo(
    currentDestination: NavDestination?,
    drawerScreens: List<AcademyDrawerDestination<out Any>>
): Pair<String, TopBarConfig> {
    // Valor por defecto
    var screenTitle = ""
    var topBarConfig = TopBarConfig()
    
    // Encontrar la pantalla activa en el drawer
    drawerScreens.forEach { destination ->
        if (currentDestination?.hierarchy?.any { it.hasRoute(destination.route::class) } == true) {
            screenTitle = destination.title
            // Configurar botones según la pantalla
            topBarConfig = when (destination.route) {
                is AcademyStudentsDestinations.AcademyStudentsGraph -> TopBarConfig(
                    showSearch = true, 
                    showFilter = true
                )
                is AcademyStudentsDestinations.InviteStudentGraph -> TopBarConfig(
                    showSearch = true,
                    showFilter = true
                )
                is AcademyTeachersDestinations.AcademyTeachersGraph -> TopBarConfig(
                    showSearch = true,
                    showFilter = true
                )
                is AcademyTeachersDestinations.InviteTeacherGraph -> TopBarConfig(
                    showSearch = true,
                    showFilter = true
                )
                is UserGraph -> TopBarConfig(
                    showSearch = false,
                    showFilter = false
                )
                else -> TopBarConfig()
            }
        }
    }
    
    return Pair(screenTitle, topBarConfig)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    title: String,
    showSearch: Boolean,
    showFilter: Boolean,
    initialSearchQuery: String,
    isActiveSearchScreen: Boolean,
    onSearchQuery: (String) -> Unit,
    onFilterClick: () -> Unit,
    onMenuClick: () -> Unit
) {
    var searchText by remember { mutableStateOf(initialSearchQuery) }
    var isFocused by remember { mutableStateOf(false) }
    var isSearchMode by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    
    // Actualizar el texto de búsqueda cuando cambia en el ViewModel
    LaunchedEffect(initialSearchQuery, isActiveSearchScreen) {
        if (isActiveSearchScreen) {
            searchText = initialSearchQuery
        }
    }
    
    TopAppBar(
        title = { 
            AnimatedVisibility(
                visible = !isSearchMode,
                enter = slideInHorizontally(
                    initialOffsetX = { -it }, // Empieza desde la izquierda
                    animationSpec = tween(300)
                ),
                exit = slideOutHorizontally(
                    targetOffsetX = { -it }, // Sale hacia la izquierda
                    animationSpec = tween(300)
                )
            ) {
                Text(title)
            }
            
            AnimatedVisibility(
                visible = isSearchMode,
                enter = slideInHorizontally(
                    initialOffsetX = { it }, // Empieza desde la derecha
                    animationSpec = tween(300)
                ),
                exit = slideOutHorizontally(
                    targetOffsetX = { it }, // Sale hacia la derecha
                    animationSpec = tween(300)
                )
            ) {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { 
                        searchText = it
                        // Solo enviar el cambio si estamos en una pantalla de búsqueda activa
                        if (isActiveSearchScreen) {
                            onSearchQuery(it)
                        }
                    },
                    modifier = Modifier
                        .fillMaxHeight(0.8f)
                        .fillMaxWidth(0.95f)
                        .focusRequester(focusRequester)
                        .onFocusChanged { 
                            isFocused = it.isFocused 
                        },
                    placeholder = { 
                        Text(
                            "Buscar",
                            color = if (isFocused) 
                                MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f) 
                            else 
                                MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                        ) 
                    },
                    leadingIcon = {
                        IconButton(
                            onClick = { 
                                isSearchMode = false
                                if (isActiveSearchScreen) {
                                    // Limpiar la búsqueda y resetear el filtro al cerrar
                                    searchText = ""
                                    onSearchQuery("")
                                }
                                focusManager.clearFocus()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver",
                                tint = if (isFocused) 
                                    MaterialTheme.colorScheme.onBackground 
                                else 
                                    MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    },
                    trailingIcon = {
                        if (searchText.isNotEmpty()) {
                            IconButton(
                                onClick = {
                                    searchText = ""
                                    if (isActiveSearchScreen) {
                                        onSearchQuery("")
                                    }
                                    focusManager.clearFocus()
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Borrar búsqueda",
                                    tint = if (isFocused) 
                                        MaterialTheme.colorScheme.onBackground 
                                    else 
                                        MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                    },
                    singleLine = true,
                    textStyle = TextStyle(
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                        color = if (isFocused) 
                            MaterialTheme.colorScheme.onBackground 
                        else 
                            MaterialTheme.colorScheme.onPrimary
                    ),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
                        focusedContainerColor = MaterialTheme.colorScheme.background,
                        unfocusedContainerColor = MaterialTheme.colorScheme.primary,
                        focusedBorderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                        unfocusedBorderColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.3f),
                        cursorColor = MaterialTheme.colorScheme.onBackground,
                        focusedLeadingIconColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedLeadingIconColor = MaterialTheme.colorScheme.onPrimary,
                        focusedPlaceholderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                        unfocusedPlaceholderColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                    )
                )
            }
        },
        navigationIcon = {
            AnimatedVisibility(
                visible = !isSearchMode,
                enter = fadeIn(animationSpec = tween(300)),
                exit = fadeOut(animationSpec = tween(300))
            ) {
                IconButton(onClick = onMenuClick) {
                    Icon(Icons.Default.Menu, contentDescription = "Menu")
                }
            }
        },
        actions = {
            AnimatedVisibility(
                visible = !isSearchMode,
                enter = fadeIn(animationSpec = tween(300)),
                exit = fadeOut(animationSpec = tween(300))
            ) {
                Row {
                    if (showSearch) {
                        IconButton(
                            onClick = { 
                                isSearchMode = true
                                // Al activar el modo búsqueda, enfocar el campo
                                // después de renderizarlo
                                if (isActiveSearchScreen) {
                                    // Intenta enfocar el campo después de que sea visible
                                    try {
                                        focusRequester.requestFocus()
                                    } catch (e: Exception) {
                                        // Ignorar errores de foco
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search, 
                                contentDescription = "Buscar"
                            )
                        }
                    }
                    
                    if (showFilter) {
                        IconButton(
                            onClick = onFilterClick
                        ) {
                            Icon(
                                imageVector = Icons.Default.FilterList, 
                                contentDescription = "Filtrar"
                            )
                        }
                    }
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
        )
    )
}

@Composable
private fun ModalDrawerContent(
    drawerScreens: List<AcademyDrawerDestination<out Any>>,
    currentDestination: NavDestination?,
    drawerNavController: NavHostController,
    onClick: () -> Unit
) {
    val groupedScreens = drawerScreens.groupBy { it.section }
    ModalDrawerSheet(
        drawerContainerColor = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(12.dp))
            groupedScreens.entries.withIndex().forEach { (index, entry) ->
                val (section, items) = entry

                Text(
                    text = section.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(LocalPadding.current.tiny)
                )

                items.forEach { item ->
                    val isSelected =
                        currentDestination?.hierarchy?.any { it.hasRoute(item.route::class) } == true

                    NavigationDrawerItem(
                        label = { Text(item.title) },
                        icon = if (isSelected) item.selectedIcon else item.unselectedIcon,
                        selected = isSelected,
                        shape = RoundedCornerShape(0),
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.onBackground,
                            unselectedTextColor = MaterialTheme.colorScheme.onBackground,
                            selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer
                        ),
                        onClick = {
                            drawerNavController.navigate(item.route) {
                                popUpTo(drawerNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                            onClick()
                        },
                    )
                }

                if (index < groupedScreens.size - 1) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = LocalPadding.current.normal),
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }

}