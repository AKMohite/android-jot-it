package com.ak.jotit.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.ak.jotit.feature.note.domain.util.ScreenRoute

internal val navigationItems = listOf(
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

internal data class NavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: ScreenRoute
)