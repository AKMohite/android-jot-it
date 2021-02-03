package com.ak.jotit.di

import android.app.Application
import androidx.room.Room
import com.ak.jotit.data.NoteDao
import com.ak.jotit.data.NotesDatabase
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
        callBack: NotesDatabase.CallBack
    ) = Room.databaseBuilder(app, NotesDatabase::class.java, "notes_db")
            .fallbackToDestructiveMigration() // TODO room migration
//            .addCallback(callBack) // TODO Remove this callback
            .build()

    @Provides
    fun provideNoteDao(db: NotesDatabase): NoteDao = db.noteDao()

    @ApplicationScope
    @Provides
    @Singleton
    fun provideAppScope()= CoroutineScope(SupervisorJob())

}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope // TODO what is this??