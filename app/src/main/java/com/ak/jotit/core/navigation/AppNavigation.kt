
package com.ak.jotit.core.navigation

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ak.jotit.feature.login.LoginScreen
import com.ak.jotit.feature.note.domain.util.ScreenRoute
import com.ak.jotit.feature.note.presentarion.addeditnote.AddEditNoteScreen
import com.ak.jotit.feature.note.presentarion.notes.NotesScreen
import com.ak.jotit.feature.splash.presentation.SplashScreen

@Composable
internal fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier
) {
    NavHost(
        navController = navController,
        startDestination = ScreenRoute.NotesScreen.route,
        modifier = modifier
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