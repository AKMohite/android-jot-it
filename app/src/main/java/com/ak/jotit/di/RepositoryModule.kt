package com.ak.jotit.di

import com.ak.jotit.data.NoteDao
import com.ak.jotit.repo.NotesRepository
import com.ak.jotit.repo.NotesRepositoryImpl
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
    fun provideNoteRepository(noteDao: NoteDao): NotesRepository = NotesRepositoryImpl(noteDao)
}