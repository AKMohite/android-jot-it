package com.ak.jotit.feature.note.domain.usecase

import com.ak.jotit.feature.note.domain.model.NoteEntity
import com.ak.jotit.feature.note.domain.repository.INotesRepository
import com.ak.jotit.feature.note.domain.util.InvalidNoteException
import kotlin.jvm.Throws

class AddNote(
    private val repository: INotesRepository
) {
    @Throws(InvalidNoteException::class)
    suspend operator fun invoke(note: NoteEntity) {
        if (note.title.isBlank()) {
            throw InvalidNoteException("The title of note cannot be empty.")
        }
        if (note.description.isBlank()) {
            throw InvalidNoteException("The description of note cannot be empty.")
        }
        repository.insertNote(note)
    }
}