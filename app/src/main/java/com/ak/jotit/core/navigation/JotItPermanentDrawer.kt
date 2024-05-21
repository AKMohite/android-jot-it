package com.ak.jotit.core.navigation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ak.jotit.ui.NavigationItem

@Composable
internal fun JotItPermanentDrawer(
    modifier: Modifier = Modifier,
    items: List<NavigationItem>,
    selectedItemIndex: Int,
    content: @Composable () -> Unit
) {
    PermanentNavigationDrawer(
        modifier = modifier,
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
    ) {
        content()
    }
}