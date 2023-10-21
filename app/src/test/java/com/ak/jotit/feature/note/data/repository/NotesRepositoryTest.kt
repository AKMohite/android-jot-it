package com.ak.jotit.feature.note.data.repository

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ak.jotit.feature.note.data.local.NoteLocalDS
import com.ak.jotit.feature.note.data.local.NotesDatabase
import com.ak.jotit.util.MainCoroutineRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class NotesRepositoryTest {

    @get:Rule
    var coroutineRule = MainCoroutineRule()

    private lateinit var appDatabase: NotesDatabase
    private lateinit var sut: NotesRepository

    @Before
    fun setup() {
        appDatabase = Room
            .inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                NotesDatabase::class.java
            )
            .allowMainThreadQueries()
            .build()

        sut = NotesRepository(local = NoteLocalDS(appDatabase.noteDao()))
    }

    @After
    fun tearDown() {
        appDatabase.close()
    }

    @Test
    fun `get notes when no notes are inserted`() = runBlocking {
        val notes: String? = null
        assertThat(notes).isEmpty()
    }
}