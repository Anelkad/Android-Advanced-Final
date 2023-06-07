package com.example.okhttp.di

import com.example.okhttp.repository.MovieRepository
import com.example.okhttp.repository.MovieRepositoryImp
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    fun providesMovieRepository(imp: MovieRepositoryImp): MovieRepository = imp

}
