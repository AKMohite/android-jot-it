package com.ak.jotit.di

import android.app.Application
import androidx.room.Room
import com.ak.jotit.data.TaskDao
import com.ak.jotit.data.TasksDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideDataBase(
        app: Application,
        callBack: TasksDatabase.CallBack
    ) = Room.databaseBuilder(app, TasksDatabase::class.java, "tasks_db")
            .fallbackToDestructiveMigration() // TODO room migration
            .addCallback(callBack) // TODO Remove this callback
            .build()

    @Provides
    fun provideTaskDao(db: TasksDatabase): TaskDao = db.taskDao()

    @ApplicationScope
    @Provides
    @Singleton
    fun provideAppScope()= CoroutineScope(SupervisorJob())

}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope // TODO what is this??