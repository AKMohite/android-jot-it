package com.ak.jotit.di

import android.app.Application
import androidx.room.Room
import com.ak.jotit.feature.note.data.local.NoteDao
import com.ak.jotit.feature.note.data.local.NotesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideDataBase(
        app: Application
    ) = Room.databaseBuilder(app, NotesDatabase::class.java, "notes_db")
            .fallbackToDestructiveMigration() // TODO room migration
            .build()

    @ApplicationScope
    @Provides
    @Singleton
    fun provideAppScope()= CoroutineScope(SupervisorJob())

}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope // TODO what is this??