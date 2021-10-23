package com.ak.jotit.feature.note.domain.util

sealed class ScreenRoute(val route: String) {
    object SplashScreen: ScreenRoute("splash_screen")
    object NotesScreen: ScreenRoute("notes_screen")
    object AddEditNoteScreen: ScreenRoute("add_edit_note_screen")
}
