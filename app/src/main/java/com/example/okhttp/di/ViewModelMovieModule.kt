package com.example.okhttp.di

import com.example.okhttp.repository.MovieRepository
import com.example.okhttp.repository.MovieRepositoryImp
import com.example.okhttp.repository.SavedMovieRepository
import com.example.okhttp.repository.SavedMovieRepositoryImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
abstract class ViewModelMovieModule {
    @Binds
    @ViewModelScoped //жизненный цикл ViewModelScope
    abstract fun bindsMovieRepository(imp: MovieRepositoryImp): MovieRepository

    @Binds
    @ViewModelScoped
    abstract fun bindsSavedMovieRepository(imp: SavedMovieRepositoryImp): SavedMovieRepository
}