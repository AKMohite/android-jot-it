package com.ak.jotit.feature.note.data.repository

import com.ak.jotit.feature.note.data.local.INoteLocalDS
import com.ak.jotit.feature.note.domain.model.NoteEntity
import com.ak.jotit.feature.note.domain.repository.INotesRepository
import kotlinx.coroutines.flow.Flow

class NotesRepository(
    private val local: INoteLocalDS
) : INotesRepository {

    override fun getAllNotes(): Flow<List<NoteEntity>> {
        return local.getAllNotes()
    }

    override suspend fun getNoteById(id: Long): NoteEntity? {
        return local.getNoteById(id)
    }

    override suspend fun insertNote(noteEntity: NoteEntity) {
        local.insertNote(noteEntity)
    }

    override suspend fun deleteNote(noteEntity: NoteEntity) {
        local.deleteNote(noteEntity)
    }

}