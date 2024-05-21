package com.ak.jotit.feature.note.presentarion.home

import androidx.compose.runtime.State
import androidx.compose.ui.focus.FocusState
import com.ak.jotit.feature.note.domain.util.NoteOrderBy
import com.ak.jotit.feature.note.presentarion.addeditnote.NoteTextFieldState
import kotlinx.coroutines.flow.StateFlow

internal interface HomeActions: DetailActions {
    val uiState: StateFlow<HomeState>
    val title: State<NoteTextFieldState>
    val description: State<NoteTextFieldState>
    val color: State<String>
    fun onOrderNotes(orderBy: NoteOrderBy)
    fun onDeleteNote(id: String)
    fun restoreDeletedNote()
    fun toggleFilter()
    fun onNoteClick(idAndColor: Pair<String, String>)
}

internal interface DetailActions {
    fun onTitleChange(title: String)
    fun onTitleFocusChange(focusState: FocusState)
    fun onDescChange(desc: String)
    fun onDescFocusChange(focusState: FocusState)
    fun onChangeColor(color: String)
    fun saveNote()
    fun onNoteSaved()
    fun closeDetail()
}
