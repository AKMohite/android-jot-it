package com.ak.jotit.feature.note.domain.usecase

data class Notes(
    val getNote: GetNote,
    val getNotes: GetNotes,
    val deleteNote: DeleteNote,
    val addNote: AddNote
)
