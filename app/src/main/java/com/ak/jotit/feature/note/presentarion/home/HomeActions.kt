package com.ak.jotit.feature.note.presentarion.home

import com.ak.jotit.feature.note.domain.util.NoteOrderBy
import kotlinx.coroutines.flow.StateFlow

internal interface HomeActions {
    val uiState: StateFlow<HomeState>
    fun onOrderNotes(orderBy: NoteOrderBy)
    fun onDeleteNote(id: String)
    fun restoreDeletedNote()
    fun toggleFilter()
    fun onNoteClick(idAndColor: Pair<String, String>)
    fun saveNote()
    fun onNoteSaved()
}