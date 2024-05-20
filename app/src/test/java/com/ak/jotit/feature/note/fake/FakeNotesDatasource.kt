package com.ak.jotit.feature.note.fake

import com.ak.jotit.feature.note.data.local.INoteLocalDS
import com.ak.jotit.feature.note.domain.model.NoteEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeNotesDatasource : INoteLocalDS {

    private val notes = mutableListOf<NoteEntity>()
    override fun getAllNotes(): Flow<List<NoteEntity>> {
        return flow { emit(notes) }
    }

    override suspend fun getNoteById(id: String): NoteEntity? {
        return notes.find { it.id == id }
    }

    override suspend fun insertNote(noteEntity: NoteEntity): String {
        if (noteEntity.id.isNotBlank()) return updateNote(noteEntity)
        val tempId = noteEntity.title
        notes.add(noteEntity.copy(id = tempId))
        return tempId
    }

    override suspend fun updateNote(noteEntity: NoteEntity): String {
        val note = notes.firstOrNull { note -> note.id == noteEntity.id } ?: throw Exception("No note found")
        notes.remove(note)
        notes.add(noteEntity)
        return noteEntity.id
    }

    override suspend fun tempDeleteNote(id: String): NoteEntity {
        val note = notes.firstOrNull { it.id == id } ?: throw IllegalStateException("Note not found")
        notes.remove(note)
        return note
    }

}
