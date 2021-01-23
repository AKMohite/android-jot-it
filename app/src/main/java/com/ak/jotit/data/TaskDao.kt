package com.ak.jotit.data

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao: BaseDao<TaskEntity> {

    @Query("SELECT * FROM task_table WHERE name LIKE '%'||:searchQuery||'%' ORDER BY is_important DESC")
    fun getTasks(searchQuery: String): Flow<List<TaskEntity>>

}