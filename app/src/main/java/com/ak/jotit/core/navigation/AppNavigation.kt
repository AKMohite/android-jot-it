
package com.ak.jotit.core.navigation

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ak.jotit.feature.login.LoginScreen
import com.ak.jotit.feature.note.domain.util.ScreenRoute
import com.ak.jotit.feature.note.presentarion.addeditnote.AddEditNoteEvent
import com.ak.jotit.feature.note.presentarion.addeditnote.AddEditNoteScreen
import com.ak.jotit.feature.note.presentarion.addeditnote.AddEditNoteViewModel
import com.ak.jotit.feature.note.presentarion.notes.NotesEvent
import com.ak.jotit.feature.note.presentarion.notes.NotesScreen
import com.ak.jotit.feature.note.presentarion.notes.NotesViewModel
import com.ak.jotit.feature.splash.presentation.SplashScreen
import kotlinx.coroutines.flow.collectLatest

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
                onNextScreen = {
                    navController
                        .navigate(ScreenRoute.NotesScreen.route){
                            popUpTo(ScreenRoute.SplashScreen.route){
                                inclusive = true
                            }
                        }
                }
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
            val viewModel: NotesViewModel = hiltViewModel()
            NotesScreen(
                state = viewModel.state.value,
                onAddEditClick = {
                    navController.navigate(ScreenRoute.AddEditNoteScreen.route)
                },
                toggleNotesFilter = {
                    viewModel.onEvent(NotesEvent.ToggleOrderSection)
                },
                onFilterChange = { filter ->
                    viewModel.onEvent(NotesEvent.OrderNotes(filter))
                },
                onNoteClick = { (id, color) ->
                    navController.navigate(ScreenRoute.AddEditNoteScreen.route + "?noteId=${id}&noteColor=${color}")
                },
                onDeleteNote = { id ->  viewModel.onEvent(NotesEvent.DeleteNote(id)) },
                onRestoreNote = { viewModel.onEvent(NotesEvent.RestoreNote) }
            )
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
            val viewModel: AddEditNoteViewModel = hiltViewModel()
            val color = entry.arguments?.getString("noteColor")
            val noteColor = if (!color.isNullOrBlank()) color else viewModel.color.value
            LaunchedEffect(key1 = true) {
                viewModel.eventFlow.collectLatest { event ->
                    when (event) {
                        is AddEditNoteViewModel.UIAddEditEvent.SaveNote -> {
                            navController.navigateUp()
                        }
                        is AddEditNoteViewModel.UIAddEditEvent.ShowSnackBar -> {
//                            TODO handle snackbar
//                            snackbarHostState.showSnackbar(
//                                message = event.message
//                            )
                        }
                    }
                }
            }
            AddEditNoteScreen(
                noteColor = noteColor,
                description = viewModel.description.value,
                title = viewModel.title.value,
                onSaveNote = {
                    viewModel.onEvent(AddEditNoteEvent.SaveNote)
                },
                onChangeColor = {
                    viewModel.onEvent(AddEditNoteEvent.ChangeColor(it))
                },
                onTitleChange = { title ->
                    viewModel.onEvent(AddEditNoteEvent.EnteredTitle(title))
                },
                onTitleFocusChange = { focusState ->
                    viewModel.onEvent(AddEditNoteEvent.ChangeTitleFocus(focusState))
                },
                onDescChange = { desc ->
                    viewModel.onEvent(AddEditNoteEvent.EnteredDescription(desc))
                },
                onDescFocusChange = { focusState ->
                    viewModel.onEvent(AddEditNoteEvent.ChangeDescriptionFocus(focusState))
                }
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