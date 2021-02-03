package com.ak.jotit.data

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao: BaseDao<NoteEntity> {

    fun getNotes(query: String, sortOrder: SortOrder, hideCompleted: Boolean): Flow<List<NoteEntity>> =
        when(sortOrder){
            SortOrder.BY_DATE -> getNotesSortedByDateCreated(query, hideCompleted)
            SortOrder.BY_NAME -> getNotesSortedByName(query, hideCompleted)
        }

    @Query("SELECT * FROM note_table WHERE id = :id")
    fun getNote(id: Long): Flow<NoteEntity>

    @Query("SELECT * FROM note_table WHERE (is_complete != :hideCompleted OR is_complete = 0) AND name LIKE '%'||:searchQuery||'%' ORDER BY is_important DESC, name")
    fun getNotesSortedByName(searchQuery: String, hideCompleted: Boolean): Flow<List<NoteEntity>>

    @Query("SELECT * FROM note_table WHERE (is_complete != :hideCompleted OR is_complete = 0) AND name LIKE '%'||:searchQuery||'%' ORDER BY is_important DESC, created_at")
    fun getNotesSortedByDateCreated(searchQuery: String, hideCompleted: Boolean): Flow<List<NoteEntity>>

    @Query("DELETE FROM note_table WHERE is_complete = 1")
    suspend fun deleteCompleteNotes()

}