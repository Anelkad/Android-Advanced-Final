package com.example.okhttp.data.di

import com.example.okhttp.data.repository.SavedMovieRepositoryImp
import com.example.okhttp.domain.repository.SavedMovieRepository
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

//    @Binds
//    @ViewModelScoped
//    abstract fun bindsSavedMovieRepository(imp: SavedMovieRepositoryImp): SavedMovieRepository
}