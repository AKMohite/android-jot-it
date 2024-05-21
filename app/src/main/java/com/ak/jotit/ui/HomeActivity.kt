package com.ak.jotit.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ak.jotit.core.navigation.AppNavigation
import com.ak.jotit.core.navigation.JotItClosedDrawer
import com.ak.jotit.core.navigation.JotItNavRail
import com.ak.jotit.core.navigation.JotItPermanentDrawer
import com.ak.jotit.core.navigation.NavigationType
import com.ak.jotit.core.navigation.navigationItems
import com.ak.jotit.ui.theme.JotItTheme
import dagger.hilt.android.AndroidEntryPoint

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
                Surface {
                    JotItApp(navigationType)
                }
            }
        }
    }
}

@Composable
internal fun JotItApp(navigationType: NavigationType) {
    var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }
    val navController = rememberNavController()

    // Subscribe to navBackStackEntry, required to get current route
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    when (navigationType) {
        NavigationType.CLOSED_DRAWER -> {
            JotItClosedDrawer(
                items = navigationItems,
                selectedItemIndex = selectedItemIndex,
                currentRoute = navBackStackEntry?.destination?.route,
                onClickItem = { index, route ->
                    selectedItemIndex = index
                    navController.navigate(route) {
                        val startRoute = navController.graph.startDestinationRoute ?: return@navigate
                        popUpTo(startRoute)
                        launchSingleTop = true
                    }
                },
                navigationType = NavigationType.CLOSED_DRAWER,
                content = { innerPadding ->
                    AppNavigation(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding)
                    )
                },
            )
        }

        NavigationType.NAVIGATION_RAIL -> {
            JotItNavRail(
                items = navigationItems,
                selectedItemIndex = selectedItemIndex,
                onClickItem = { index, route ->
                    selectedItemIndex = index
                    navController.navigate(route) {
                        val startRoute = navController.graph.startDestinationRoute ?: return@navigate
                        popUpTo(startRoute)
                        launchSingleTop = true
                    }
                }
            ) {
                AppNavigation(
                    navController = navController,
                    modifier = Modifier.padding(PaddingValues(8.dp)),
                    navigationType = navigationType
                )
            }
        }

        NavigationType.PERMANENT_DRAWER -> {
            JotItPermanentDrawer(
                items = navigationItems,
                selectedItemIndex = selectedItemIndex,
                onClickItem = { index, route ->
                    selectedItemIndex = index
                    navController.navigate(route) {
                        val startRoute = navController.graph.startDestinationRoute ?: return@navigate
                        popUpTo(startRoute)
                        launchSingleTop = true
                    }
                }
            ) {
                AppNavigation(
                    navController = navController,
                    modifier = Modifier.padding(PaddingValues(12.dp)),
                    navigationType = navigationType
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun JotItPreview() {
    JotItTheme {
        JotItApp(
            navigationType = NavigationType.CLOSED_DRAWER
        )
    }
}

@Preview(showBackground = true, widthDp = 700)
@Composable
fun ReplyAppPreviewTablet() {
    JotItTheme {
        JotItApp(
            navigationType = NavigationType.NAVIGATION_RAIL
        )
    }
}

@Preview(showBackground = true, widthDp = 1000)
@Composable
fun ReplyAppPreviewDesktop() {
    JotItTheme {
        JotItApp(
            navigationType = NavigationType.PERMANENT_DRAWER
        )
    }
}


