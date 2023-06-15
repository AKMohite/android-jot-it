package com.ak.jotit.feature.note.domain.usecase

import com.ak.jotit.feature.note.data.repository.FakeNoteRepository
import com.ak.jotit.feature.note.domain.model.NoteEntity
import com.ak.jotit.feature.note.domain.util.NoteOrderBy
import com.ak.jotit.feature.note.domain.util.OrderType
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class GetNotesTest {

    private lateinit var getNotes: GetNotes
    private lateinit var fakeRepo: FakeNoteRepository

    @Before
    fun setUp() {
        fakeRepo = FakeNoteRepository()
        getNotes = GetNotes(fakeRepo)

        val notesToInsert = mutableListOf<NoteEntity>()
        ('a'..'z').forEachIndexed { index, c ->
            notesToInsert.add(
                NoteEntity(
                    id = index.toString(),
                    title = c.toString(),
                    description = c.toString(),
                    color = "${index}_${c}_color",
                    createdAt = index.toLong(),
                    updatedAt = index.toLong()
                )
            )
        }
        notesToInsert.shuffle()
        runBlocking {
            notesToInsert.forEach { note ->
                fakeRepo.insertNote(note)
            }
        }
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `Order notes by title ascending, correct order`() = runBlocking {
        val notes = getNotes(NoteOrderBy.Title(OrderType.Ascending)).first()
        for (i in 0..notes.size - 2) {
            assertThat(notes[i].title).isLessThan(notes[i+1].title)
        }
    }

    @Test
    fun `Order notes by title descending, correct order`() = runBlocking {
        val notes = getNotes(NoteOrderBy.Title(OrderType.Descending)).first()
        for (i in 0..notes.size - 2) {
            assertThat(notes[i].title).isGreaterThan(notes[i+1].title)
        }
    }

    @Test
    fun `Order notes by date ascending, correct order`() = runBlocking {
        val notes = getNotes(NoteOrderBy.Date(OrderType.Ascending)).first()
        for (i in 0..notes.size - 2) {
            assertThat(notes[i].updatedAt).isLessThan(notes[i+1].updatedAt)
        }
    }

    @Test
    fun `Order notes by date descending, correct order`() = runBlocking {
        val notes = getNotes(NoteOrderBy.Date(OrderType.Descending)).first()
        for (i in 0..notes.size - 2) {
            assertThat(notes[i].updatedAt).isGreaterThan(notes[i+1].updatedAt)
        }
    }

    @Test
    fun `Order notes by color ascending, correct order`() = runBlocking {
        val notes = getNotes(NoteOrderBy.Color(OrderType.Ascending)).first()
        for (i in 0..notes.size - 2) {
            assertThat(notes[i].color).isLessThan(notes[i+1].color)
        }
    }

    @Test
    fun `Order notes by color descending, correct order`() = runBlocking {
        val notes = getNotes(NoteOrderBy.Color(OrderType.Descending)).first()
        for (i in 0..notes.size - 2) {
            assertThat(notes[i].color).isGreaterThan(notes[i+1].color)
        }
    }
}

