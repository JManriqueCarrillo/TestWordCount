package com.example.testwordcount.di

import android.content.Context
import com.example.testwordcount.repository.FilesRepository
import com.example.testwordcount.repository.impl.FilesRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    fun providesFilesRepository(@ApplicationContext context: Context): FilesRepository = FilesRepositoryImpl(context)
}