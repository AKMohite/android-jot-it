package com.ak.jotit.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class TaskDaoTest {

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: TasksDatabase
    private lateinit var dao: TaskDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            TasksDatabase::class.java
        ).allowMainThreadQueries()
            .build()

        dao = database.taskDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertTaskItem() = runBlockingTest {
        val taskItem = TaskEntity(id = 1, name = "Check Test", isImportant = true)
        dao.insert(taskItem)
        val task = dao.getTask(1).first()

        assertThat<TaskEntity>(task as TaskEntity, notNullValue())
        assertThat(task.id, `is`(taskItem.id))
        assertThat(task.name, `is`(taskItem.name))
        assertThat(task.createdAt, `is`(taskItem.createdAt))
        assertThat(task.isComplete, `is`(taskItem.isComplete))
    }

    @Test
    fun updateTaskItem_verifySuccess() = runBlockingTest {
        // When inserting a task
        val taskItem = TaskEntity(id = 1, name = "Check Test", isImportant = true)
        dao.insert(taskItem)

        // When the task is updated
        val updatedTask = TaskEntity(name = "new title", id= taskItem.id, isComplete = true)
        dao.update(updatedTask)

        // THEN - The loaded data contains the expected values
        val task = dao.getTask(1).first()
        assertThat(task?.id, `is`(taskItem.id))
        assertThat(task?.name, `is`("new title"))
        assertThat(task?.isComplete, `is`(true))
    }

    @Test
    fun deleteTaskItem_verifySuccess() = runBlockingTest {
        // When inserting a task
        val taskItem = TaskEntity(id = 1, name = "Check Test", isImportant = true)
        dao.insert(taskItem)

        // When the task is updated
        dao.delete(taskItem)

        // THEN - The loaded data contains the expected values
        val task = dao.getTask(1).first()
        assertThat<TaskEntity>(task, `is`(nullValue()))
    }

}