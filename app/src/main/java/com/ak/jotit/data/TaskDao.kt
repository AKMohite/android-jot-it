package com.ak.jotit.data

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao: BaseDao<TaskEntity> {

    fun getTasks(query: String, sortOrder: SortOrder, hideCompleted: Boolean): Flow<List<TaskEntity>> =
        when(sortOrder){
            SortOrder.BY_DATE -> getTasksSortedByDateCreated(query, hideCompleted)
            SortOrder.BY_NAME -> getTasksSortedByName(query, hideCompleted)
        }

    @Query("SELECT * FROM task_table WHERE (is_complete != :hideCompleted OR is_complete = 0) AND name LIKE '%'||:searchQuery||'%' ORDER BY is_important DESC, name")
    fun getTasksSortedByName(searchQuery: String, hideCompleted: Boolean): Flow<List<TaskEntity>>

    @Query("SELECT * FROM task_table WHERE (is_complete != :hideCompleted OR is_complete = 0) AND name LIKE '%'||:searchQuery||'%' ORDER BY is_important DESC, created_at")
    fun getTasksSortedByDateCreated(searchQuery: String, hideCompleted: Boolean): Flow<List<TaskEntity>>

}