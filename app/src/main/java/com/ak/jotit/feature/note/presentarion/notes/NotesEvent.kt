package com.ak.jotit.feature.note.presentarion.notes

import com.ak.jotit.feature.note.domain.model.NoteEntity
import com.ak.jotit.feature.note.domain.util.NoteOrderBy

sealed class NotesEvent {
    data class OrderNotes(val noteOrderBy: NoteOrderBy): NotesEvent()
    data class DeleteNote(val noteId: String): NotesEvent()
    object RestoreNote: NotesEvent()
    object ToggleOrderSection: NotesEvent()
}
