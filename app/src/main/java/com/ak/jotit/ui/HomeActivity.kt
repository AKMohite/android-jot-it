package com.ak.jotit.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ak.jotit.core.navigation.AppNavigation
import com.ak.jotit.core.navigation.JotItClosedDrawer
import com.ak.jotit.core.navigation.JotItNavRail
import com.ak.jotit.core.navigation.JotItPermanentDrawer
import com.ak.jotit.core.navigation.NavigationType
import com.ak.jotit.feature.note.domain.util.ScreenRoute
import com.ak.jotit.ui.theme.JotItTheme
import dagger.hilt.android.AndroidEntryPoint

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
                            JotItClosedDrawer(
                                items = items,
                                selectedItemIndex = selectedItemIndex
                            ) { innerPadding ->
                                AppNavigation(
                                    navController= navController,
                                    modifier = Modifier.padding(innerPadding)
                                )
                            }
                        }
                        NavigationType.NAVIGATION_RAIL -> {
                            JotItNavRail(
                                items = items,
                                selectedItemIndex = selectedItemIndex
                            ) {
                                AppNavigation(
                                    navController= navController,
                                    modifier = Modifier.padding(PaddingValues(8.dp)),
                                    navigationType = navigationType
                                )
                            }
                        }
                        NavigationType.PERMANENT_DRAWER -> {
                            JotItPermanentDrawer(
                                items = items,
                                selectedItemIndex = selectedItemIndex
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


