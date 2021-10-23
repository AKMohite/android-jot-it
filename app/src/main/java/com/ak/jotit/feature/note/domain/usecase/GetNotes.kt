package com.ak.jotit.feature.note.domain.usecase

import com.ak.jotit.feature.note.domain.model.NoteEntity
import com.ak.jotit.feature.note.domain.repository.INotesRepository
import com.ak.jotit.feature.note.domain.util.NoteOrderBy
import com.ak.jotit.feature.note.domain.util.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetNotes (
    private val repository: INotesRepository
) {

    operator fun invoke(
        noteOrderBy: NoteOrderBy = NoteOrderBy.Date(OrderType.Descending)
    ): Flow<List<NoteEntity>> {
        return repository.getAllNotes()
            .map { notes ->
                when (noteOrderBy.orderType) {
                    is OrderType.Ascending -> {
                        when (noteOrderBy) {
                            is NoteOrderBy.Title -> notes.sortedBy { note -> note.title.lowercase() }
                            is NoteOrderBy.Date -> notes.sortedBy { note -> note.updatedAt }
                            is NoteOrderBy.Color -> notes.sortedBy { note -> note.color }
                        }
                    }
                    is OrderType.Descending -> {
                        when (noteOrderBy) {
                            is NoteOrderBy.Title -> notes.sortedByDescending { note -> note.title.lowercase() }
                            is NoteOrderBy.Date -> notes.sortedByDescending { note -> note.updatedAt }
                            is NoteOrderBy.Color -> notes.sortedByDescending { note -> note.color }
                        }
                    }
                }
            }
    }

}