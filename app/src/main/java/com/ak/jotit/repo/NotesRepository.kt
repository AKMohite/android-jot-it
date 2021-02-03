package com.ak.jotit.repo

import com.ak.jotit.data.SortOrder
import com.ak.jotit.data.NoteEntity
import kotlinx.coroutines.flow.Flow

interface NotesRepository {

    fun getNotes(query: String, sortOrder: SortOrder, hideCompleted: Boolean): Flow<List<NoteEntity>>

    suspend fun insertNote(noteEntity: NoteEntity)

    suspend fun updateNote(noteEntity: NoteEntity)

    suspend fun deleteNote(noteEntity: NoteEntity)

    suspend fun deleteCompletedNotes()

}