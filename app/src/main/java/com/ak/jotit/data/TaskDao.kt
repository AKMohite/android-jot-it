package com.ak.jotit.data

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao: BaseDao<TaskEntity> {

    @Query("SELECT * FROM task_table")
    fun getTasks(): Flow<List<TaskEntity>>

}