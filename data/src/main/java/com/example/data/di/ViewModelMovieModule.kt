package com.example.data.di

import com.example.data.repository.SavedMovieRepositoryImp
import com.example.domain.repository.SavedMovieRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class ViewModelMovieModule {
//    @Binds
//    @ViewModelScoped //жизненный цикл ViewModelScope
//    abstract fun bindsMovieRepository(imp: MovieRepositoryImp): MovieRepository

    @Binds
    @ViewModelScoped
    abstract fun bindsSavedMovieRepository(imp: SavedMovieRepositoryImp): SavedMovieRepository
}