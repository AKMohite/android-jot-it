package com.ak.jotit.di

import com.ak.jotit.feature.note.data.local.NoteDao
import com.ak.jotit.feature.note.domain.repository.INotesRepository
import com.ak.jotit.feature.note.data.repository.NotesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Singleton
    @Provides
    fun provideNoteRepository(noteDao: NoteDao): INotesRepository = NotesRepository(noteDao)
}