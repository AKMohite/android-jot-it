package com.ak.jotit.ui.addeditnote

import androidx.hilt.Assisted
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ak.jotit.feature.note.domain.model.NoteEntity
import com.ak.jotit.repo.NotesRepository
import com.ak.jotit.ui.ADD_NOTE_RESULT_OK
import com.ak.jotit.ui.EDIT_NOTE_RESULT_OK
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val notesRepository: NotesRepository,
    @Assisted private val state: SavedStateHandle
): ViewModel() {

    val note = state.get<NoteEntity>("note") // this argument should be same in nav_graph

    var noteTitle = state.get<String>("noteTitle") ?: note?.name ?: ""
        set(value) {
            field = value
            state.set("noteTitle", value)
        }

    var noteDesc = state.get<String>("noteDesc") ?: note?.description ?: ""
        set(value) {
            field = value
            state.set("noteDesc", value)
        }

    var noteImportance = state.get<Boolean>("noteImportance") ?: note?.isImportant ?: false
        set(value) {
            field = value
            state.set("noteImportance", value)
        }

    private val _addEditNoteEventChannel = Channel<AddEditNoteEvent>()
    val addEditNoteEventChannel = _addEditNoteEventChannel.receiveAsFlow()

    fun onSaveClick(){
        if (noteTitle.isBlank()) {
//            show invalid input msg
            showInvalidInputMessage("Title cannot be empty")
            return
        }

        if (noteDesc.isBlank()) {
            showInvalidInputMessage("Description cannot be empty")
            return
        }

        if (note != null) {
            val updatedNote = note.copy(name = noteTitle, description = noteDesc, isImportant = noteImportance, updatedAt = System.currentTimeMillis())
            updatedNote(updatedNote)
        } else {
            val newNote = NoteEntity(name = noteTitle, description = noteDesc, isImportant = noteImportance, color = 0)
            createNote(newNote)
        }
    }

    private fun showInvalidInputMessage(msg: String) = viewModelScope.launch {
        _addEditNoteEventChannel.send(AddEditNoteEvent.ShowInvalidInputMessage(msg))
    }

    private fun createNote(newNote: NoteEntity) = viewModelScope.launch {
        notesRepository.insertNote(newNote)
//        navigate back
        _addEditNoteEventChannel.send(AddEditNoteEvent.NavigateBackResult(ADD_NOTE_RESULT_OK))
    }

    private fun updatedNote(updatedNote: NoteEntity) = viewModelScope.launch {
        notesRepository.insertNote(updatedNote)
//        navigate back
        _addEditNoteEventChannel.send(AddEditNoteEvent.NavigateBackResult(EDIT_NOTE_RESULT_OK))
    }

    sealed class AddEditNoteEvent{
        data class ShowInvalidInputMessage(val msg: String): AddEditNoteEvent()
        data class NavigateBackResult(val result: Int): AddEditNoteEvent()
    }

}