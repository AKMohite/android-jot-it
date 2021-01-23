package com.ak.jotit.di

import com.ak.jotit.data.TaskDao
import com.ak.jotit.repo.TasksRepository
import com.ak.jotit.repo.TasksRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object RepositoryModule {

    @Singleton
    @Provides
    fun provideTaskRepository(taskDao: TaskDao): TasksRepository = TasksRepositoryImpl(taskDao)
}