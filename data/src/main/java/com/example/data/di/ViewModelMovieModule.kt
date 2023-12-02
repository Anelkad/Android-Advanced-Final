package com.example.data.di

import com.example.data.repository.MovieRepositoryImp
import com.example.data.repository.SavedMovieRepositoryImp
import com.example.domain.repository.MovieRepository
import com.example.domain.repository.SavedMovieRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn

@Module
//@InstallIn(ViewModelComponent::class)
abstract class ViewModelMovieModule {
    @Binds //жизненный цикл ViewModelScope
    abstract fun bindsMovieRepository(imp: MovieRepositoryImp): MovieRepository

    @Binds
    abstract fun bindsSavedMovieRepository(imp: SavedMovieRepositoryImp): SavedMovieRepository
}