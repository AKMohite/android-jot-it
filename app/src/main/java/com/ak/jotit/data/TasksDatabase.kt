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
    entities = [TaskEntity::class],
    version = 1
)
abstract class TasksDatabase: RoomDatabase() {

    abstract fun taskDao(): TaskDao

    class CallBack @Inject constructor(
        private val database: Provider<TasksDatabase>,
        @ApplicationScope private val appScope: CoroutineScope
    ): RoomDatabase.Callback(){

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
//            dummy data with dbOperations
            val dao = database.get().taskDao()

            appScope.launch {
                dao.insert(TaskEntity(name = "Go for walk"))
                dao.insert(TaskEntity(name = "Drink water"))
                dao.insert(TaskEntity(name = "Fix issues", isImportant = true))
                dao.insert(TaskEntity(name = "Standup call"))
                dao.insert(TaskEntity(name = "Create POC"))
                dao.insert(TaskEntity(name = "Read a book/blog"))
                dao.insert(TaskEntity(name = "Get some sleep", isImportant = true))
            }
        }

    }

}