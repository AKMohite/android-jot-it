package com.ak.jotit.feature.note.domain.usecase

import com.ak.jotit.feature.note.domain.model.NoteEntity
import com.ak.jotit.feature.note.domain.repository.INotesRepository

class GetNote(
    private val repository: INotesRepository
) {

    suspend operator fun invoke(id: Long): NoteEntity? {
        return repository.getNoteById(id)
    }

}