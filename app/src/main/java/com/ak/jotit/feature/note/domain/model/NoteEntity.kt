package com.ak.jotit.feature.note.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ak.jotit.ui.theme.*

// TODO implement add edit viewmodel instead of passing objects
@Entity(tableName = "note_table")
data class NoteEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: String,  // TODO implement sync functionality using ids
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "is_synced")
    val isSynced: Boolean = false,
    @ColumnInfo(name = "is_deleted")
    val isDeleted: Int = 0, // 0 => no, 1 => tempdelete, -1 => permanentdelete
    @ColumnInfo(name = "color")
    val color: String,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long = System.currentTimeMillis(),
) {
}