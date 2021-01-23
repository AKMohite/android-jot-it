package com.ak.jotit.repo

import com.ak.jotit.data.SortOrder
import com.ak.jotit.data.TaskEntity
import kotlinx.coroutines.flow.Flow

interface TasksRepository {

    fun getTasks(query: String, sortOrder: SortOrder, hideCompleted: Boolean): Flow<List<TaskEntity>>

    suspend fun insertTask(taskEntity: TaskEntity)

    suspend fun updateTask(taskEntity: TaskEntity)

    suspend fun deleteTask(taskEntity: TaskEntity)

    suspend fun deleteCompleteTasks()

}