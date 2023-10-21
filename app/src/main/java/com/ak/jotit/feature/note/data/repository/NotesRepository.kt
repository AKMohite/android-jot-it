package com.ak.jotit.feature.note.data.repository

import com.ak.jotit.feature.note.data.local.INoteLocalDS
import com.ak.jotit.feature.note.domain.model.NoteEntity
import com.ak.jotit.feature.note.domain.repository.INotesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

internal class NotesRepository(
    private val local: INoteLocalDS
) : INotesRepository {

    override fun getAllNotes(): Flow<List<NoteEntity>> {
        return local.getAllNotes()
    }

    override suspend fun getNoteById(id: String): NoteEntity? = withContext(Dispatchers.IO) {
        local.getNoteById(id)
    }

    override suspend fun insertNote(noteEntity: NoteEntity): Unit = withContext(Dispatchers.IO) {
        local.insertNote(noteEntity)
    }

    override suspend fun tempDelete(noteEntity: NoteEntity): Unit = withContext(Dispatchers.IO) {
        local.tempDeleteNote(noteEntity)
    }

}