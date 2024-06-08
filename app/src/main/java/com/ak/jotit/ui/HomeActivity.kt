package com.ak.jotit.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.window.layout.FoldingFeature
import androidx.window.layout.WindowInfoTracker
import com.ak.jotit.core.navigation.AppNavigation
import com.ak.jotit.core.navigation.DevicePosture
import com.ak.jotit.core.navigation.JotItClosedDrawer
import com.ak.jotit.core.navigation.JotItContentType
import com.ak.jotit.core.navigation.JotItNavRail
import com.ak.jotit.core.navigation.JotItPermanentDrawer
import com.ak.jotit.core.navigation.NavigationType
import com.ak.jotit.core.navigation.navigationItems
import com.ak.jotit.feature.note.domain.util.ScreenRoute
import com.ak.jotit.ui.theme.JotItTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@AndroidEntryPoint
class HomeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val devicePostureFlow = WindowInfoTracker.getOrCreate(this).windowLayoutInfo(this)
            .flowWithLifecycle(this.lifecycle)
            .map { layoutIndo ->
                val foldingFeature = layoutIndo.displayFeatures
                    .filterIsInstance<FoldingFeature>()
                    .firstOrNull()
                when {
                    isBookPosture(foldingFeature) -> DevicePosture.BookPosture(foldingFeature.bounds)
                    isSeparating(foldingFeature) -> DevicePosture.Separating(foldingFeature.bounds, foldingFeature.orientation)
                    else -> DevicePosture.NormalPosture
                }
            }.stateIn(
                scope = lifecycleScope,
                started = SharingStarted.Eagerly,
                initialValue = DevicePosture.NormalPosture
            )
        setContent {
            JotItTheme {
                val windowSize = calculateWindowSizeClass(activity = this)
                val devicePosture = devicePostureFlow.collectAsState().value
                val contentType: JotItContentType
                val navigationType: NavigationType
                when(windowSize.widthSizeClass) {
                    WindowWidthSizeClass.Compact -> {
                        navigationType = NavigationType.CLOSED_DRAWER
                        contentType = JotItContentType.SINGLE_PANE
                    }
                    WindowWidthSizeClass.Medium -> {
                        navigationType = NavigationType.NAVIGATION_RAIL
                        contentType = if (devicePosture is DevicePosture.BookPosture || devicePosture is DevicePosture.Separating) {
                            JotItContentType.DUAL_PANE
                        } else {
                            JotItContentType.SINGLE_PANE
                        }
                    }
                    WindowWidthSizeClass.Expanded -> {
                        navigationType = if (devicePosture is DevicePosture.BookPosture) {
                            NavigationType.NAVIGATION_RAIL
                        } else {
                            NavigationType.PERMANENT_DRAWER
                        }
                        contentType = JotItContentType.DUAL_PANE
                    }
                    else -> {
                        navigationType = NavigationType.CLOSED_DRAWER
                        contentType = JotItContentType.SINGLE_PANE
                    }
                }
                Surface {
                    JotItApp(
                        navigationType = navigationType,
                        contentType = contentType
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalContracts::class)
    fun isBookPosture(foldFeature: FoldingFeature?): Boolean {
        contract { returns(true) implies (foldFeature != null) }
        return foldFeature?.state == FoldingFeature.State.HALF_OPENED &&
                foldFeature.orientation == FoldingFeature.Orientation.VERTICAL
    }

    @OptIn(ExperimentalContracts::class)
    fun isSeparating(foldFeature: FoldingFeature?): Boolean {
        contract { returns(true) implies (foldFeature != null) }
        return foldFeature?.state == FoldingFeature.State.FLAT && foldFeature.isSeparating
    }
}

@Composable
internal fun JotItApp(
    navigationType: NavigationType,
    contentType: JotItContentType
) {
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
            ) { innerPadding ->
                AppNavigation(
                    navController = navController,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentType = contentType
                )
            }
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
                },
                onAddNoteClick = {
                    navController.navigate(ScreenRoute.AddEditNoteScreen.route)
                }
            ) {
                AppNavigation(
                    navController = navController,
                    modifier = Modifier
                        .fillMaxSize(),
                    navigationType = navigationType,
                    contentType = contentType
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
                },
                content = {
                    AppNavigation(
                        navController = navController,
                        modifier = Modifier
                            .fillMaxSize(),
                        navigationType = navigationType,
                        contentType = contentType
                    )
                },
                onAddNoteClick = {
                    navController.navigate(ScreenRoute.AddEditNoteScreen.route)
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun JotItPreview() {
    JotItTheme {
        JotItApp(
            navigationType = NavigationType.CLOSED_DRAWER,
            contentType = JotItContentType.SINGLE_PANE
        )
    }
}

@Preview(showBackground = true, widthDp = 700)
@Composable
fun ReplyAppPreviewTablet() {
    JotItTheme {
        JotItApp(
            navigationType = NavigationType.NAVIGATION_RAIL,
            contentType = JotItContentType.SINGLE_PANE
        )
    }
}

@Preview(showBackground = true, widthDp = 1000)
@Composable
fun ReplyAppPreviewDesktop() {
    JotItTheme {
        JotItApp(
            navigationType = NavigationType.PERMANENT_DRAWER,
            contentType = JotItContentType.DUAL_PANE
        )
    }
}


