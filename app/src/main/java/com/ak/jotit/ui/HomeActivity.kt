package com.ak.jotit.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ak.jotit.feature.note.domain.util.ScreenRoute
import com.ak.jotit.feature.note.presentarion.addeditnote.AddEditNoteScreen
import com.ak.jotit.feature.note.presentarion.notes.NotesScreen
import com.ak.jotit.feature.splash.presentation.SplashScreen
import com.ak.jotit.ui.theme.JotItTheme
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalAnimationApi
@AndroidEntryPoint
class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JotItTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = ScreenRoute.SplashScreen.route
                ) {
                    composable(route = ScreenRoute.SplashScreen.route) {
                        SplashScreen(
                            navController = navController
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
                }
            }
        }
    }
}