package com.ak.jotit.di

import com.ak.jotit.feature.note.data.local.NotesDatabase
import com.ak.jotit.feature.note.data.repository.NotesRepository
import com.ak.jotit.feature.note.domain.repository.INotesRepository
import com.ak.jotit.feature.note.domain.usecase.*
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
    fun provideNoteRepository(db: NotesDatabase): INotesRepository = NotesRepository(db.noteDao())

    @Singleton
    @Provides
    fun provideNoteUseCase(repository: INotesRepository): Notes {
        return Notes(
            getNote = GetNote(repository),
            getNotes = GetNotes(repository),
            deleteNote = DeleteNote(repository),
            addNote = AddNote(repository)
        )
    }
}