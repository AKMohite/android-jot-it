package com.ak.jotit.feature.note.data.repository

import com.ak.jotit.feature.note.domain.model.NoteEntity
import com.ak.jotit.feature.note.domain.repository.INotesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeNoteRepository : INotesRepository {

    private val notes = mutableListOf<NoteEntity>()

    override fun getAllNotes(): Flow<List<NoteEntity>> {
        return flow { emit(notes) }
    }

    override suspend fun getNoteById(id: String): NoteEntity? {
        return notes.find { it.id == id }
    }

    override suspend fun insertNote(noteEntity: NoteEntity) {
        notes.add(noteEntity)
    }

    override suspend fun tempDelete(noteEntity: NoteEntity) {
        notes.remove(noteEntity)
    }

}