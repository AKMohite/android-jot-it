package com.ak.jotit.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ak.jotit.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

// TODO export schema of DB
// TODO add typeConverters
@Database(
    entities = [NoteEntity::class],
    version = 1
)
abstract class NotesDatabase: RoomDatabase() {

    abstract fun noteDao(): NoteDao

    class CallBack @Inject constructor(
        private val database: Provider<NotesDatabase>,
        @ApplicationScope private val appScope: CoroutineScope
    ): RoomDatabase.Callback(){

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
//            dummy data with dbOperations
            val dao = database.get().noteDao()

            appScope.launch {
                /*dao.insert(NoteEntity(name = "Go for walk"))
                dao.insert(NoteEntity(name = "Drink water", isComplete = true))
                dao.insert(NoteEntity(name = "Fix issues", isImportant = true))
                dao.insert(NoteEntity(name = "Standup call"))
                dao.insert(NoteEntity(name = "Create POC"))
                dao.insert(NoteEntity(name = "Read a book/blog"))
                dao.insert(NoteEntity(name = "Get some sleep", isImportant = true))*/
            }
        }

    }

}