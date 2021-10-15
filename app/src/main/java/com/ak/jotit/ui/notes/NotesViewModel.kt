package com.ak.jotit.ui.notes

import androidx.hilt.Assisted
import androidx.lifecycle.*
import com.ak.jotit.data.PrefManager
import com.ak.jotit.data.SortOrder
import com.ak.jotit.feature.note.domain.model.NoteEntity
import com.ak.jotit.repo.NotesRepository
import com.ak.jotit.ui.ADD_NOTE_RESULT_OK
import com.ak.jotit.ui.EDIT_NOTE_RESULT_OK
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@FlowPreview
@HiltViewModel
class NotesViewModel @Inject constructor(
    private val notesRepository: NotesRepository,
    private val prefManager: PrefManager,
    @Assisted private val state: SavedStateHandle
): ViewModel() {

    val searchQuery = state.getLiveData("searchQuery", "")

    val preferencesFlow = prefManager.preferencesFlow

    private val _notesEventChannel = Channel<NotesEvent>()
    val notesEventChannel = _notesEventChannel.receiveAsFlow()

    private val notesFlow = combine(
        searchQuery.asFlow(),
        preferencesFlow
    ){ query, filterPreferences ->
        Pair(query, filterPreferences)
    }
        .flatMapLatest { (query, filterPreferences)->
            notesRepository.getNotes(query, filterPreferences.sortOrder, filterPreferences.hideCompleted)
        }

    val notes = notesFlow.asLiveData()

    fun onSortOrderSelected(sortOrder: SortOrder) = viewModelScope.launch {
        prefManager.updateSortOrder(sortOrder)
    }

    fun onHideCompleteCheck(onHideComplete: Boolean) = viewModelScope.launch {
        prefManager.updateHideCompleted(onHideComplete)
    }

    fun onNoteCheckChanged(note: NoteEntity, isChecked: Boolean) = viewModelScope.launch {
        notesRepository.updateNote(note.copy(isComplete = isChecked))
    }

    fun onNoteSelected(note: NoteEntity) = viewModelScope.launch {
        _notesEventChannel.send(NotesEvent.NavigateToEditNoteScreen(note))
    }

    fun onNoteSwiped(note: NoteEntity) = viewModelScope.launch {
        notesRepository.deleteNote(note)
        _notesEventChannel.send(NotesEvent.ShowUndoDeleteNoteMessage(note))
    }

    fun onUndoDeleteClick(note: NoteEntity) = viewModelScope.launch {
        notesRepository.insertNote(note)
    }

    fun onAddNewNoteClick() = viewModelScope.launch {
        _notesEventChannel.send(NotesEvent.NavigateToAddNoteScreen)
    }

    fun onAddEditResult(result: Int) {
        when(result){
            ADD_NOTE_RESULT_OK -> showNoteSaveConfirmationMessage("Note added")

            EDIT_NOTE_RESULT_OK -> showNoteSaveConfirmationMessage("Note updated")
        }
    }

    private fun showNoteSaveConfirmationMessage(msg: String) = viewModelScope.launch {
        _notesEventChannel.send(NotesEvent.ShowNoteSavedConfirmationMsg(msg))
    }

    fun onDeleteAllCompleted() = viewModelScope.launch {
        _notesEventChannel.send(NotesEvent.NavigateToDeleteAllCompleteScreen)
    }

    sealed class NotesEvent{
        data class ShowUndoDeleteNoteMessage(val note: NoteEntity): NotesEvent()
        object NavigateToAddNoteScreen: NotesEvent()
        object NavigateToDeleteAllCompleteScreen: NotesEvent()
        data class NavigateToEditNoteScreen(val note: NoteEntity): NotesEvent()
        data class ShowNoteSavedConfirmationMsg(val msg: String): NotesEvent()
    }

}