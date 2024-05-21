package com.ak.jotit.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ak.jotit.R
import com.ak.jotit.core.navigation.AppNavigation
import com.ak.jotit.feature.note.domain.util.ScreenRoute
import com.ak.jotit.ui.NavigationType.CLOSED_DRAWER
import com.ak.jotit.ui.NavigationType.NAVIGATION_RAIL
import com.ak.jotit.ui.NavigationType.PERMANENT_DRAWER
import com.ak.jotit.ui.theme.JotItTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

data class NavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: ScreenRoute
)

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@AndroidEntryPoint
class HomeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JotItTheme {
                val windowSize = calculateWindowSizeClass(activity = this)
                val navigationType = when(windowSize.widthSizeClass) {
                    WindowWidthSizeClass.Compact -> NavigationType.CLOSED_DRAWER
                    WindowWidthSizeClass.Medium -> NavigationType.NAVIGATION_RAIL
                    WindowWidthSizeClass.Expanded -> NavigationType.PERMANENT_DRAWER
                    else -> NavigationType.CLOSED_DRAWER
                }
                val items = listOf(
                    NavigationItem(
                        title = "Notes",
                        selectedIcon = Icons.Filled.Home,
                        unselectedIcon = Icons.Outlined.Home,
                        route = ScreenRoute.NotesScreen
                    ),
                    NavigationItem(
                        title = "Trash",
                        selectedIcon = Icons.Filled.Delete,
                        unselectedIcon = Icons.Outlined.Delete,
                        route = ScreenRoute.DeletedNotesScreen
                    ),
                    NavigationItem(
                        title = "Settings",
                        selectedIcon = Icons.Filled.Settings,
                        unselectedIcon = Icons.Outlined.Settings,
                        route = ScreenRoute.SettingsScreen
                    )
                )
                Surface {
                    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                    val scope = rememberCoroutineScope()
                    var selectedItemIndex by rememberSaveable { mutableStateOf(0) }
                    var isDrawerAccessible by rememberSaveable { mutableStateOf(false) }
                    val navController = rememberNavController()

                    // Subscribe to navBackStackEntry, required to get current route
                    val navBackStackEntry by navController.currentBackStackEntryAsState()

                    isDrawerAccessible = when {
                        navigationType == NavigationType.NAVIGATION_RAIL || navigationType == NavigationType.PERMANENT_DRAWER -> false
                        listOf(ScreenRoute.NotesScreen.route, ScreenRoute.DeletedNotesScreen.route, ScreenRoute.SettingsScreen.route).contains(navBackStackEntry?.destination?.route) -> true
                        else -> false
                    }

                    when (navigationType) {
                        NavigationType.CLOSED_DRAWER -> {
                            ModalNavigationDrawer(
                                gesturesEnabled = isDrawerAccessible,
                                drawerContent = {
                                    ModalDrawerSheet {
                                        Spacer(modifier = Modifier.height(16.dp))
                                        items.forEachIndexed { index, item ->
                                            NavigationDrawerItem(
                                                label = {
                                                    Text(text = item.title)
                                                },
                                                selected = index == selectedItemIndex,
                                                onClick = {
                                                    selectedItemIndex = index
                                                    scope.launch {
                                                        drawerState.close()
                                                    }
                                                    navController.navigate(item.route.route) {
                                                        val startRoute = navController.graph.startDestinationRoute ?: return@navigate
                                                        popUpTo(startRoute)
                                                        launchSingleTop = true
                                                    }
                                                },
                                                icon = {
                                                    Icon(
                                                        imageVector = if (index == selectedItemIndex) {
                                                            item.selectedIcon
                                                        } else {
                                                            item.unselectedIcon
                                                        }, contentDescription = item.title)
                                                },
                                                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
//                                        badge = {
//                                            Text(text = item.badgeCount)
//                                        }
                                            )
                                        }
                                    }
                                },
                                drawerState = drawerState
                            ) {
                                Scaffold(
                                    topBar = {
                                        AnimatedVisibility(
                                            visible = isDrawerAccessible,
//                                    enter = slideInVertically(initialOffsetY = { -it }),
//                                    exit = slideOutVertically(targetOffsetY = { -it }),
                                        ) {
                                            TopAppBar(
                                                title = {
                                                    Text(text = stringResource(id = R.string.app_name))
                                                },
                                                navigationIcon = {
                                                    IconButton(onClick = {
                                                        scope.launch {
                                                            drawerState.open()
                                                        }
                                                    }) {
                                                        Icon(
                                                            imageVector = Icons.Default.Menu,
                                                            contentDescription = stringResource(id = R.string.top_bar_menu)
                                                        )
                                                    }
                                                }
                                            )
                                        }
                                    },
                                ) { innerPadding ->
                                    AppNavigation(
                                        navController= navController,
                                        modifier = Modifier.padding(innerPadding)
                                    )
                                }
                            }
                        }
                        NavigationType.NAVIGATION_RAIL -> {
                            Row {
                                JotItNavRail(
                                    items = items,
                                    selectedItemIndex = selectedItemIndex
                                )
                                AppNavigation(
                                    navController= navController,
                                    modifier = Modifier.padding(PaddingValues(8.dp)),
                                    navigationType = navigationType
                                )
                            }
                        }
                        NavigationType.PERMANENT_DRAWER -> {
                            PermanentNavigationDrawer(
                                drawerContent = {
                                    PermanentDrawerSheet {
                                        Spacer(modifier = Modifier.height(16.dp))
                                        items.forEachIndexed { index, item ->
                                            NavigationDrawerItem(
                                                label = {
                                                    Text(text = item.title)
                                                },
                                                selected = index == selectedItemIndex,
                                                onClick = {
                                                    selectedItemIndex = index
                                                    scope.launch {
                                                        drawerState.close()
                                                    }
                                                    navController.navigate(item.route.route) {
                                                        val startRoute = navController.graph.startDestinationRoute ?: return@navigate
                                                        popUpTo(startRoute)
                                                        launchSingleTop = true
                                                    }
                                                },
                                                icon = {
                                                    Icon(
                                                        imageVector = if (index == selectedItemIndex) {
                                                            item.selectedIcon
                                                        } else {
                                                            item.unselectedIcon
                                                        }, contentDescription = item.title)
                                                },
                                                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
//                                        badge = {
//                                            Text(text = item.badgeCount)
//                                        }
                                            )
                                        }
                                    }
                                },
                            ) {
                                AppNavigation(
                                    navController= navController,
                                    modifier = Modifier.padding(PaddingValues(12.dp)),
                                    navigationType = navigationType
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun JotItNavRail(
    modifier: Modifier = Modifier,
    items: List<NavigationItem>,
    selectedItemIndex: Int,
) {
    NavigationRail(
        modifier = modifier
    ) {
        items.forEachIndexed { index, item ->
            NavigationRailItem(
                label = {
                    Text(text = item.title)
                },
                selected = index == selectedItemIndex,
                onClick = {
//                        selectedItemIndex = index
//                        scope.launch {
//                            drawerState.close()
//                        }
//                        navController.navigate(item.route.route) {
//                            val startRoute = navController.graph.startDestinationRoute ?: return@navigate
//                            popUpTo(startRoute)
//                            launchSingleTop = true
//                        }
                },
                icon = {
                    Icon(
                        imageVector = if (index == selectedItemIndex) {
                            item.selectedIcon
                        } else {
                            item.unselectedIcon
                        }, contentDescription = item.title)
                },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
//                                        badge = {
//                                            Text(text = item.badgeCount)
//                                        }
            )
        }
    }
}

/**
 * [CLOSED_DRAWER] -> for compact devices such as mobile phones
 * [NAVIGATION_RAIL] -> for medium devices such as tablets and foldables
 * [PERMANENT_DRAWER] -> for large devices such as tablets and desktops
 */
internal enum class NavigationType {
    CLOSED_DRAWER,
    NAVIGATION_RAIL,
    PERMANENT_DRAWER
}