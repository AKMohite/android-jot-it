package com.ak.jotit.core.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ak.jotit.R
import com.ak.jotit.feature.note.domain.util.ScreenRoute
import kotlinx.coroutines.launch

@Composable
internal fun JotItClosedDrawer(
    modifier: Modifier = Modifier,
    items: List<NavigationItem>,
    selectedItemIndex: Int,
    onClickItem: (Int, String) -> Unit,
    navigationType: NavigationType,
    currentRoute: String? = null,
    content: @Composable (PaddingValues) -> Unit,
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var isDrawerAccessible by rememberSaveable { mutableStateOf(false) }
    isDrawerAccessible = when {
        navigationType == NavigationType.NAVIGATION_RAIL || navigationType == NavigationType.PERMANENT_DRAWER -> false
        listOf(
            ScreenRoute.NotesScreen.route,
            ScreenRoute.DeletedNotesScreen.route,
            ScreenRoute.SettingsScreen.route
        ).contains(currentRoute) -> true

        else -> false
    }
    ModalNavigationDrawer(
        modifier = modifier,
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
                            scope.launch {
                                drawerState.close()
                            }
                            onClickItem(index, item.route.route)
                        },
                        icon = {
                            Icon(
                                imageVector = if (index == selectedItemIndex) {
                                    item.selectedIcon
                                } else {
                                    item.unselectedIcon
                                }, contentDescription = item.title
                            )
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
            content(innerPadding)
        }
    }
}