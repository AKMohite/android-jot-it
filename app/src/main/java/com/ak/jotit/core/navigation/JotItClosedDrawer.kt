package com.ak.jotit.core.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ak.jotit.ui.NavigationItem

@Composable
internal fun JotItClosedDrawer(
    modifier: Modifier = Modifier,
    items: List<NavigationItem>,
    selectedItemIndex: Int,
    content: @Composable (PaddingValues) -> Unit
) {
    ModalNavigationDrawer(
        modifier = modifier,
//        gesturesEnabled = isDrawerAccessible,
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
//                            selectedItemIndex = index
//                            scope.launch {
//                                drawerState.close()
//                            }
//                            navController.navigate(item.route.route) {
//                                val startRoute = navController.graph.startDestinationRoute ?: return@navigate
//                                popUpTo(startRoute)
//                                launchSingleTop = true
//                            }
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
//        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
//                AnimatedVisibility(
//                    visible = isDrawerAccessible,
////                                    enter = slideInVertically(initialOffsetY = { -it }),
////                                    exit = slideOutVertically(targetOffsetY = { -it }),
//                ) {
//                    TopAppBar(
//                        title = {
//                            Text(text = stringResource(id = R.string.app_name))
//                        },
//                        navigationIcon = {
//                            IconButton(onClick = {
//                                scope.launch {
//                                    drawerState.open()
//                                }
//                            }) {
//                                Icon(
//                                    imageVector = Icons.Default.Menu,
//                                    contentDescription = stringResource(id = R.string.top_bar_menu)
//                                )
//                            }
//                        }
//                    )
//                }
            },
        ) { innerPadding ->
            content(innerPadding)
        }
    }
}