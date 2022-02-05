package com.ak.jotit.feature.note.presentarion.addeditnote

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ak.jotit.feature.note.domain.model.NoteEntity
import com.ak.jotit.feature.note.domain.usecase.Notes
import com.ak.jotit.feature.note.domain.util.InvalidNoteException
import com.ak.jotit.util.DESCRIPTION_FIELD
import com.ak.jotit.util.TITLE_FIELD
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val notes: Notes,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _title = mutableStateOf(NoteTextFieldState(
        hint = TITLE_FIELD
    ))
    val title: State<NoteTextFieldState> = _title

    private val _description = mutableStateOf(NoteTextFieldState(
        hint = DESCRIPTION_FIELD
    ))
    val description: State<NoteTextFieldState> = _description

    private val _color = mutableStateOf(NoteEntity.noteColors.random().toArgb())
    val color: State<Int> = _color

    private val _eventFlow = MutableSharedFlow<UIAddEditEvent>()
    val eventFlow: SharedFlow<UIAddEditEvent> = _eventFlow

    private var currentNoteId: String = ""

    init {
        savedStateHandle.get<String>("noteId")?.let { noteId ->
            if (!noteId.isNullOrBlank()) {
                getNoteById(noteId)
            }
        }
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
            }
        }
    }

    fun onEvent(event: AddEditNoteEvent) {
        when (event) {
            is AddEditNoteEvent.EnteredTitle -> {
                _title.value = title.value.copy(
                    text = event.value
                )
            }
            is AddEditNoteEvent.ChangeTitleFocus -> {
                _title.value = title.value.copy(
                    isHintVisible = !event.focusState.isFocused && title.value.text.isBlank()
                )
            }
            is AddEditNoteEvent.EnteredDescription -> {
                _description.value = description.value.copy(
                    text = event.value
                )
            }
            is AddEditNoteEvent.ChangeDescriptionFocus -> {
                _description.value = description.value.copy(
                    isHintVisible = !event.focusState.isFocused && title.value.text.isBlank()
                )
            }
            is AddEditNoteEvent.ChangeColor -> {
                _color.value = event.color
            }
            is AddEditNoteEvent.SaveNote -> {
                saveNote()
            }
        }
    }

    private fun saveNote() {
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
                _eventFlow.emit(UIAddEditEvent.SaveNote)
            } catch (e: InvalidNoteException) {
                _eventFlow.emit(UIAddEditEvent.ShowSnackBar(
                    message = e.message ?: "Couldn't save note"
                ))
            }
        }
    }


    sealed class UIAddEditEvent {
        data class ShowSnackBar(val message: String) : UIAddEditEvent()
        object SaveNote: UIAddEditEvent()
    }
}