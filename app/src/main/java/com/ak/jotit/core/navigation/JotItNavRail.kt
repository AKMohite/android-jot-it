package com.ak.jotit.core.navigation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ak.jotit.ui.NavigationItem

@Composable
internal fun JotItNavRail(
    modifier: Modifier = Modifier,
    items: List<NavigationItem>,
    selectedItemIndex: Int,
    content: @Composable () -> Unit
) {
    Row {
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
        content()
    }
}