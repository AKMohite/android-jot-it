package com.ak.jotit.feature.note.domain.repository

import com.ak.jotit.data.SortOrder
import com.ak.jotit.feature.note.domain.model.NoteEntity
import kotlinx.coroutines.flow.Flow

interface INotesRepository {

    fun getAllNotes(): Flow<List<NoteEntity>>

    suspend fun getNoteById(id: Long): NoteEntity?

    fun getNotes(query: String, sortOrder: SortOrder, hideCompleted: Boolean): Flow<List<NoteEntity>>

    suspend fun insertNote(noteEntity: NoteEntity)

    suspend fun updateNote(noteEntity: NoteEntity)

    suspend fun deleteNote(noteEntity: NoteEntity)

    suspend fun deleteCompletedNotes()

}