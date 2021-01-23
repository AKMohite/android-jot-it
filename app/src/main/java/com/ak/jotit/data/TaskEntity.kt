package com.ak.jotit.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.text.DateFormat

// TODO implement add edit viewmodel instead of passing objects
@Entity(tableName = "task_table")
@Parcelize // to pass object between fragments
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,  // TODO implement sync functionality using ids
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "is_important")
    val isImportant: Boolean = false,
    @ColumnInfo(name = "is_complete")
    val isComplete: Boolean = false,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis() // TODO add updatedAt column
): Parcelable{
    val createdDateFormatted: String
        get() = DateFormat.getDateInstance().format(createdAt) // TODO use Room TypeConverters
}