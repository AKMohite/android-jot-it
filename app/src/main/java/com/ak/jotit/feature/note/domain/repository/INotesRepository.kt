package com.ak.jotit.feature.note.domain.repository

import com.ak.jotit.feature.note.domain.model.NoteEntity
import kotlinx.coroutines.flow.Flow

interface INotesRepository {

    fun getAllNotes(): Flow<List<NoteEntity>>

    suspend fun getNoteById(id: String): NoteEntity?

    suspend fun insertNote(noteEntity: NoteEntity)

    suspend fun tempDelete(id: String): NoteEntity?
}