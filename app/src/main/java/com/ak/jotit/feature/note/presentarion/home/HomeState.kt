package com.ak.jotit.feature.note.presentarion.home

import com.ak.jotit.feature.note.domain.model.NoteEntity
import com.ak.jotit.feature.note.domain.util.NoteOrderBy
import com.ak.jotit.feature.note.domain.util.OrderType

internal data class HomeState(
    val notes: List<NoteEntity> = emptyList(),
    val noteOrderBy: NoteOrderBy = NoteOrderBy.Date(OrderType.Descending),
    val isOrderSectionVisible: Boolean = false,
    val openedNote: NoteEntity? = null,
    val isDetailOnlyOpen: Boolean = false,
    val loading: Boolean = false,
    val error: String? = null
) {

    fun getNoteDetail(): NoteEntity {
        return openedNote ?: throw IllegalStateException("Note is not found but state has found note")
    }
    fun canShowDetail(): Boolean = openedNote != null && isDetailOnlyOpen
}