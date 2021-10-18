package com.ak.jotit.feature.note.domain.util

sealed class ScreenRoute(val route: String) {
    object NotesScreen: ScreenRoute("notes_screen")
    object AddEditNoteScreen: ScreenRoute("add_edit_note_screen")
}
