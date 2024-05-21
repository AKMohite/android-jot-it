package com.ak.jotit.feature.note.presentarion.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.focus.FocusState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ak.jotit.feature.note.domain.model.NoteEntity
import com.ak.jotit.feature.note.domain.usecase.Notes
import com.ak.jotit.feature.note.domain.util.InvalidNoteException
import com.ak.jotit.feature.note.domain.util.NoteOrderBy
import com.ak.jotit.feature.note.domain.util.OrderType
import com.ak.jotit.feature.note.presentarion.addeditnote.AddEditNoteViewModel
import com.ak.jotit.feature.note.presentarion.addeditnote.NoteTextFieldState
import com.ak.jotit.ui.theme.getRandomColor
import com.ak.jotit.util.DESCRIPTION_FIELD
import com.ak.jotit.util.TITLE_FIELD
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    private val notes: Notes,
    savedStateHandle: SavedStateHandle
): ViewModel(), HomeActions {
    private val _uiState = MutableStateFlow(HomeState(loading = true))
    override val uiState: StateFlow<HomeState> = _uiState

//    region Home
    private var recentDeletedNote: NoteEntity? = null
    private var getNotesJob: Job? = null
//    endregion

//    region detail
    private var currentNoteId= savedStateHandle.get<String>("noteId") ?: ""
    private val _title = mutableStateOf(
        NoteTextFieldState(
        hint = TITLE_FIELD
    )
    )
    override val title: State<NoteTextFieldState> = _title
    private val _description = mutableStateOf(
        NoteTextFieldState(
        hint = DESCRIPTION_FIELD
    )
    )
    override val description: State<NoteTextFieldState> = _description
    private val _color = mutableStateOf(getRandomColor().colorName)
    override val color: State<String> = _color
//    endregion

    init {
        getNotes(NoteOrderBy.Date(OrderType.Descending))
    }

//    region Home

    override fun onOrderNotes(orderBy: NoteOrderBy) {
        if (uiState.value.noteOrderBy == orderBy
            && uiState.value.noteOrderBy.orderType == orderBy.orderType
        ) {
            return
        }
        getNotes(orderBy)
    }

    override fun onDeleteNote(id: String) {
        deleteNote(id)
    }

    override fun restoreDeletedNote() {
        restoreNote()
    }

    override fun toggleFilter() {
        _uiState.value = uiState.value.copy(
            isOrderSectionVisible = !uiState.value.isOrderSectionVisible
        )
    }

    private fun getNotes(noteOrderBy: NoteOrderBy) {
        getNotesJob?.cancel()
        getNotesJob = notes.getNotes(noteOrderBy)
            .onEach { notes ->
                _uiState.value = uiState.value.copy(
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

    private fun deleteNote(id: String) {
        viewModelScope.launch {
            val deletedNote = notes.deleteNote(id)
            recentDeletedNote = deletedNote
        }
    }

    override fun onNoteClick(idAndColor: Pair<String, String>) {
        val (id, color) = idAndColor
        getNoteById(id)
    }
//    endregion

//    region detail
    override fun onTitleChange(title: String) {
        _title.value = this.title.value.copy(
            text = title
        )
    }

    override fun onTitleFocusChange(focusState: FocusState) {
        _title.value = title.value.copy(
            isHintVisible = !focusState.isFocused && title.value.text.isBlank()
        )
    }
    override fun onDescChange(desc: String) {
        _description.value = description.value.copy(
            text = desc
        )
    }
    override fun onDescFocusChange(focusState: FocusState) {
        _description.value = description.value.copy(
            isHintVisible = !focusState.isFocused && title.value.text.isBlank()
        )
    }

    override fun onChangeColor(color: String) {
        _color.value = color
    }
    override fun saveNote() {
    viewModelScope.launch {
        try {
            notes.addNote(
                NoteEntity(
                    title = title.value.text,
                    description = description.value.text,
                    color = color.value,
                    id = currentNoteId
                )
            )
//            _eventFlow.emit(AddEditNoteViewModel.UIAddEditEvent.SaveNote)
        } catch (e: InvalidNoteException) {
//            _eventFlow.emit(
//                AddEditNoteViewModel.UIAddEditEvent.ShowSnackBar(
//                message = e.message ?: "Couldn't save note"
//            ))
        }
    }
    }

    override fun onNoteSaved() {
        TODO("Not yet implemented")
    }
    private fun getNoteById(noteId: String) {
        viewModelScope.launch {
            notes.getNote(noteId)?.also { note ->
                currentNoteId = note.id
                _title.value= title.value.copy(
                    text = note.title,
                    isHintVisible = false
                )
                _description.value = description.value.copy(
                    text = note.description,
                    isHintVisible = false
                )
                _color.value = note.color
                _uiState.value = uiState.value.copy(
                    openedNote = note
                )
            }
        }
    }
//    endregion

}