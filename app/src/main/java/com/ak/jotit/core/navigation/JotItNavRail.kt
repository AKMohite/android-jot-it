package com.ak.jotit.core.navigation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ak.jotit.R

@Composable
internal fun JotItNavRail(
    modifier: Modifier = Modifier,
    items: List<NavigationItem>,
    selectedItemIndex: Int,
    onClickItem: (Int, String) -> Unit,
    onAddNoteClick: () -> Unit,
    content: @Composable () -> Unit,
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
            Spacer(modifier = Modifier.height(8.dp))
            FloatingActionButton(
                onClick = onAddNoteClick,
                modifier = Modifier.padding(top = 8.dp, bottom = 32.dp),
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.add_note),
                    modifier = Modifier.size(18.dp)
                )
            }
        }
        content()
    }
}