package com.ak.jotit.feature.note.data.local

import androidx.room.Dao
import androidx.room.Query
import com.ak.jotit.data.SortOrder
import com.ak.jotit.feature.note.domain.model.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao: BaseDao<NoteEntity> {

    fun getNotes(query: String, sortOrder: SortOrder, hideCompleted: Boolean): Flow<List<NoteEntity>> =
        when(sortOrder){
            SortOrder.BY_DATE -> getNotesSortedByDateCreated(query, hideCompleted)
            SortOrder.BY_NAME -> getNotesSortedByName(query, hideCompleted)
        }

    @Query("SELECT * FROM note_table")
    fun getAllNotes(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM note_table WHERE id = :id")
    suspend fun getNoteById(id: Long): NoteEntity?

    @Query("SELECT * FROM note_table WHERE id = :id")
    fun getNote(id: Long): Flow<NoteEntity>

    @Query("SELECT * FROM note_table WHERE (is_complete != :hideCompleted OR is_complete = 0) AND (title LIKE '%'||:searchQuery||'%' OR description LIKE '%'||:searchQuery||'%') ORDER BY is_important DESC, title")
    fun getNotesSortedByName(searchQuery: String, hideCompleted: Boolean): Flow<List<NoteEntity>>

    @Query("SELECT * FROM note_table WHERE (is_complete != :hideCompleted OR is_complete = 0) AND (title LIKE '%'||:searchQuery||'%' OR description LIKE '%'||:searchQuery||'%') ORDER BY is_important DESC, created_at")
    fun getNotesSortedByDateCreated(searchQuery: String, hideCompleted: Boolean): Flow<List<NoteEntity>>

    @Query("DELETE FROM note_table WHERE is_complete = 1")
    suspend fun deleteCompleteNotes()

}