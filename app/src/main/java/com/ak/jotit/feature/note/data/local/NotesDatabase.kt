package com.ak.jotit.feature.note.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ak.jotit.feature.note.domain.model.NoteEntity

// TODO export schema of DB
// TODO add typeConverters
@Database(
    entities = [NoteEntity::class],
    version = 1
)
abstract class NotesDatabase: RoomDatabase() {

    abstract fun noteDao(): NoteDao

}