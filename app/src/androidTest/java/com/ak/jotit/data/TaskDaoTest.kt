package com.ak.jotit.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
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
        val allTasks = dao.getTasks("", SortOrder.BY_DATE, false).first()
        val firstTask = allTasks[0]

        assertThat<TaskEntity>(firstTask as TaskEntity, notNullValue())
        assertThat(firstTask.id, `is`(taskItem.id))
        assertThat(firstTask.name, `is`(taskItem.name))
        assertThat(firstTask.createdAt, `is`(taskItem.createdAt))
        assertThat(firstTask.isComplete, `is`(taskItem.isComplete))
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
        val loaded = dao.getTasks("", SortOrder.BY_DATE, false).first()
        val firstTask = loaded[0]
        assertThat(firstTask?.id, `is`(taskItem.id))
        assertThat(firstTask?.name, `is`("new title"))
        assertThat(firstTask?.isComplete, `is`(true))
    }

}