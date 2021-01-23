package com.ak.jotit.repo

import com.ak.jotit.data.SortOrder
import com.ak.jotit.data.TaskDao
import com.ak.jotit.data.TaskEntity
import kotlinx.coroutines.flow.Flow

class TasksRepositoryImpl(private val taskDao: TaskDao) : TasksRepository{
    override fun getTasks(
        query: String,
        sortOrder: SortOrder,
        hideCompleted: Boolean
    ): Flow<List<TaskEntity>> {
        return taskDao.getTasks(query, sortOrder, hideCompleted)
    }

    override suspend fun insertTask(taskEntity: TaskEntity) {
        taskDao.insert(taskEntity)
    }

    override suspend fun updateTask(taskEntity: TaskEntity) {
        taskDao.update(taskEntity)
    }

    override suspend fun deleteTask(taskEntity: TaskEntity) {
        taskDao.delete(taskEntity)
    }

    override suspend fun deleteCompleteTasks() {
        taskDao.deleteCompleteTasks()
    }

}