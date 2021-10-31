package com.ak.jotit.feature.note.data.local

import androidx.room.Dao
import androidx.room.Query
import com.ak.jotit.feature.note.domain.model.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao: BaseDao<NoteEntity> {

    @Query("SELECT * FROM note_table WHERE is_deleted = 0")
    fun getAllNotes(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM note_table WHERE id = :id")
    suspend fun getNoteById(id: String): NoteEntity?

    @Query("SELECT * FROM note_table WHERE id = :id")
    fun getNote(id: Long): Flow<NoteEntity>

}