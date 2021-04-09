package com.ak.jotit.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
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
class NoteDaoTest {

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: NotesDatabase
    private lateinit var dao: NoteDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            NotesDatabase::class.java
        ).allowMainThreadQueries()
            .build()

        dao = database.noteDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertNoteItem() = runBlockingTest {
        val noteItem = NoteEntity(id = 1, name = "Check Test", isImportant = true, description = "Testing description")
        dao.insert(noteItem)
        val note = dao.getNote(1).first()

        assertThat<NoteEntity>(note as NoteEntity, notNullValue())
        assertThat(note.id, `is`(noteItem.id))
        assertThat(note.name, `is`(noteItem.name))
        assertThat(note.createdAt, `is`(noteItem.createdAt))
        assertThat(note.isComplete, `is`(noteItem.isComplete))
    }

    @Test
    fun updateNoteItem_verifySuccess() = runBlockingTest {
        // When inserting a note
        val noteItem = NoteEntity(id = 1, name = "Check Test", isImportant = true, description = "Testing description")
        dao.insert(noteItem)

        // When the note is updated
        val updatedNote = NoteEntity(name = "new title", id= noteItem.id, isComplete = true, description = "new description")
        dao.update(updatedNote)

        // THEN - The loaded data contains the expected values
        val note = dao.getNote(1).first()
        assertThat(note?.id, `is`(noteItem.id))
        assertThat(note?.name, `is`("new title"))
        assertThat(note?.isComplete, `is`(true))
    }

    @Test
    fun deleteNoteItem_verifySuccess() = runBlockingTest {
        // When inserting a note
        val noteItem = NoteEntity(id = 1, name = "Check Test", isImportant = true, description = "Testing description")
        dao.insert(noteItem)

        // When the note is updated
        dao.delete(noteItem)

        // THEN - The loaded data contains the expected values
        val note = dao.getNote(1).first()
        assertThat<NoteEntity>(note, `is`(nullValue()))
    }

}