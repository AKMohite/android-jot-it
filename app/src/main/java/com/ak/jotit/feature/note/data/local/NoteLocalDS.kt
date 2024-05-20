package com.ak.jotit.feature.note.data.local

import com.ak.jotit.feature.note.domain.model.NoteEntity
import kotlinx.coroutines.flow.Flow
import java.util.*

internal class NoteLocalDS(
    private val noteDao: NoteDao
): INoteLocalDS {

    companion object {
        private const val PREFIX_TEMP_NOTE_ID = "temp-id-"
        fun generateTemporaryId() = "$PREFIX_TEMP_NOTE_ID${UUID.randomUUID()}"
    }

    override fun getAllNotes(): Flow<List<NoteEntity>> {
        return noteDao.getAllNotes()
    }

    override suspend fun getNoteById(id: String): NoteEntity? {
        return noteDao.getNoteById(id)
    }

    override suspend fun insertNote(noteEntity: NoteEntity): String {
        if (!noteEntity.id.isNullOrBlank()) return updateNote(noteEntity)
        val tempId = generateTemporaryId()
        noteDao.insert(noteEntity.copy(id = tempId))
        return tempId
    }

    override suspend fun updateNote(noteEntity: NoteEntity): String {
        noteDao.update(noteEntity)
        return noteEntity.id
    }



    override suspend fun tempDeleteNote(id: String): NoteEntity? {
        val noteEntity = getNoteById(id) ?: return null
        noteDao.update(noteEntity.copy(isDeleted = 1))
        return noteEntity
    }
}

interface INoteLocalDS {

    fun getAllNotes(): Flow<List<NoteEntity>>

    suspend fun getNoteById(id: String): NoteEntity?

    suspend fun insertNote(noteEntity: NoteEntity): String

    suspend fun updateNote(noteEntity: NoteEntity): String

    suspend fun tempDeleteNote(id: String): NoteEntity?
}