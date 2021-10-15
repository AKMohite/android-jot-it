package com.ak.jotit.feature.note.domain.usecase

data class Notes(
    val getNotes: GetNotes,
    val deleteNote: DeleteNote,
    val addNote: AddNote
)
