package com.ak.jotit.feature.note.data.repository

import com.ak.jotit.data.SortOrder
import com.ak.jotit.feature.note.data.local.NoteDao
import com.ak.jotit.feature.note.domain.model.NoteEntity
import com.ak.jotit.feature.note.domain.repository.INotesRepository
import kotlinx.coroutines.flow.Flow

class NotesRepository(
    private val noteDao: NoteDao
) : INotesRepository {

    override fun getAllNotes(): Flow<List<NoteEntity>> {
        return noteDao.getAllNotes()
    }

    override suspend fun getNoteById(id: Long): NoteEntity? {
        return noteDao.getNoteById(id)
    }

    override fun getNotes(
        query: String,
        sortOrder: SortOrder,
        hideCompleted: Boolean
    ): Flow<List<NoteEntity>> {
        return noteDao.getNotes(query, sortOrder, hideCompleted)
    }

    override suspend fun insertNote(noteEntity: NoteEntity) {
        noteDao.insert(noteEntity)
    }

    override suspend fun updateNote(noteEntity: NoteEntity) {
        noteDao.update(noteEntity)
    }

    override suspend fun deleteNote(noteEntity: NoteEntity) {
        noteDao.delete(noteEntity)
    }

    override suspend fun deleteCompletedNotes() {
        noteDao.deleteCompleteNotes()
    }

}