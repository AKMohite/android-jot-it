package com.ak.jotit.feature.note.domain.usecase

import com.ak.jotit.feature.note.domain.model.NoteEntity
import com.ak.jotit.feature.note.domain.repository.INotesRepository

class DeleteNote (
    private val repository: INotesRepository
) {

    suspend operator fun invoke(
        id: String
    ): NoteEntity? {
        return repository.tempDelete(id)
    }

}