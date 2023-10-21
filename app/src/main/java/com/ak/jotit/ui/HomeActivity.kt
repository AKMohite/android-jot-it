package com.ak.jotit.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ak.jotit.R
import com.ak.jotit.feature.login.LoginScreen
import com.ak.jotit.feature.note.domain.util.ScreenRoute
import com.ak.jotit.feature.note.presentarion.addeditnote.AddEditNoteScreen
import com.ak.jotit.feature.note.presentarion.notes.NotesScreen
import com.ak.jotit.feature.splash.presentation.SplashScreen
import com.ak.jotit.ui.theme.JotItTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

data class NavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: ScreenRoute
)

@ExperimentalAnimationApi
@AndroidEntryPoint
class HomeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JotItTheme {
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
                    var selectedItemIndex by rememberSaveable {
                        mutableStateOf(0)
                    }
                    val navController = rememberNavController()
                    ModalNavigationDrawer(
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
                                            navController.navigate(item.route.route)
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
                            },
                        ) { innerPadding ->
                            NavHost(
                                navController = navController,
                                startDestination = ScreenRoute.NotesScreen.route,
                                modifier = Modifier.padding(innerPadding)
                            ) {

                                composable(route = ScreenRoute.SplashScreen.route) {
                                    SplashScreen(
                                        navController = navController
                                    )
                                }

                                composable(route = ScreenRoute.LoginScreen.route) {
                                    LoginScreen(
                                        onAuthenticate = {
                                            navController.navigate(ScreenRoute.NotesScreen.route) {
                                                popUpTo(ScreenRoute.LoginScreen.route) {
                                                    inclusive = true
                                                }
                                            }
                                        },
                                        onSignup = {
                                            navController.navigate(ScreenRoute.SignupScreen.route)
                                        }
                                    )
                                }

                                composable(
                                    route = ScreenRoute.NotesScreen.route
                                ) {
                                    NotesScreen(navController = navController)
                                }
                                composable(
                                    route = ScreenRoute.AddEditNoteScreen.route + "?noteId={noteId}&noteColor={noteColor}",
                                    arguments = listOf(
                                        navArgument(
                                            name = "noteId"
                                        ) {
                                            type = NavType.StringType
                                            defaultValue = ""
                                        },
                                        navArgument(
                                            name = "noteColor"
                                        ) {
                                            type = NavType.StringType
                                            defaultValue = ""
                                        }
                                    )
                                ) { entry ->
                                    val color = entry.arguments?.getString("noteColor")
                                    AddEditNoteScreen(
                                        navController = navController,
                                        noteColor = color
                                    )
                                }

                                composable(route = ScreenRoute.DeletedNotesScreen.route) {
                                    Text(
                                        text = "Deleted Notes",
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }

                                composable(route = ScreenRoute.SettingsScreen.route) {
                                    Text(
                                        text = "Settings",
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}