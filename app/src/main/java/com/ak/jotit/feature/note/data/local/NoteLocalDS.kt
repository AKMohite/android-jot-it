package com.ak.jotit.feature.note.data.local

import com.ak.jotit.feature.note.domain.model.NoteEntity
import kotlinx.coroutines.flow.Flow

class NoteLocalDS(
    private val noteDao: NoteDao
): INoteLocalDS {
    override fun getAllNotes(): Flow<List<NoteEntity>> {
        return noteDao.getAllNotes()
    }

    override suspend fun getNoteById(id: Long): NoteEntity? {
        return noteDao.getNoteById(id)
    }

    override suspend fun insertNote(noteEntity: NoteEntity) {
        noteDao.insert(noteEntity)
    }

    override suspend fun deleteNote(noteEntity: NoteEntity) {
        noteDao.delete(noteEntity)
    }
}

interface INoteLocalDS {

    fun getAllNotes(): Flow<List<NoteEntity>>

    suspend fun getNoteById(id: Long): NoteEntity?

    suspend fun insertNote(noteEntity: NoteEntity)

    suspend fun deleteNote(noteEntity: NoteEntity)
}