package com.ak.jotit.feature.note.presentarion.notes

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ak.jotit.feature.note.domain.model.NoteEntity
import com.ak.jotit.feature.note.domain.usecase.Notes
import com.ak.jotit.feature.note.domain.util.NoteOrderBy
import com.ak.jotit.feature.note.domain.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val notes: Notes
): ViewModel() {

    private val _state = mutableStateOf(NotesState())
    val state: State<NotesState> = _state

    private var recentDeletedNote: NoteEntity? = null

    private var getNotesJob: Job? = null

    init {
        getNotes(NoteOrderBy.Date(OrderType.Descending))
    }

    fun onEvent(event: NotesEvent) {
        when (event) {
            is NotesEvent.DeleteNote -> {
                deleteNote(event.note)
            }
            is NotesEvent.OrderNotes -> {
                if (state.value.noteOrderBy::class == event.noteOrderBy::class
                    && state.value.noteOrderBy.orderType == event.noteOrderBy.orderType
                ) {
                    return
                }
                getNotes(event.noteOrderBy)
            }
            is NotesEvent.RestoreNote -> {
                restoreNote()
            }
            is NotesEvent.ToggleOrderSection -> {
                _state.value = state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }
        }
    }

    private fun getNotes(noteOrderBy: NoteOrderBy) {
        getNotesJob?.cancel()
        getNotesJob = notes.getNotes(noteOrderBy)
            .onEach { notes ->
                _state.value = state.value.copy(
                    notes = notes,
                    noteOrderBy = noteOrderBy
                )
            }
            .launchIn(viewModelScope)
    }

    private fun restoreNote() {
        viewModelScope.launch {
            notes.addNote(recentDeletedNote ?: return@launch)
            recentDeletedNote = null
        }
    }

    private fun deleteNote(note: NoteEntity) {
        viewModelScope.launch {
            notes.deleteNote(note)
            recentDeletedNote = note
        }
    }

}