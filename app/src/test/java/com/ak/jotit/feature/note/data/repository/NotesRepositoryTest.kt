package com.ak.jotit.feature.note.data.repository

import com.ak.jotit.feature.note.domain.model.NoteEntity
import com.ak.jotit.feature.note.domain.repository.INotesRepository
import com.ak.jotit.feature.note.fake.FakeNotesDatasource
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test


class NotesRepositoryTest {

    private lateinit var sut: INotesRepository

    @Before
    fun setup() {

        sut = NotesRepository(local = FakeNotesDatasource())
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `get notes when no notes are inserted`() = runTest {
        val notes = sut.getAllNotes().first()
        assertThat(notes).isEmpty()
    }

    @Test
    fun `insert note should return when fetch`() = runTest{
        val note = createNoteFor("A")
        sut.insertNote(note)

        val notes = sut.getAllNotes().first()

        assertThat(notes.first().id).isNotEmpty()
        assertThat(notes.first().title).isEqualTo(note.title)
        assertThat(notes.first().description).isEqualTo(note.description)
    }

    @Test
    fun `update inserted note should return with updated note`() = runTest{
        val note = createNoteFor("A")
        sut.insertNote(note)
        val notesList = sut.getAllNotes().first()
        val insertedNote = notesList.first()

        sut.insertNote(insertedNote)

        val notes = sut.getAllNotes().first()

        assertThat(notes.first().title).isEqualTo(note.title)
        assertThat(notes.first().description).isEqualTo(note.description)
    }

    private fun createNoteFor(title: String): NoteEntity {
        return NoteEntity(id = "", title = title, description = title, color = "")
    }
}