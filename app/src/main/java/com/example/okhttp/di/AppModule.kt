package com.example.okhttp.di

import FIREBASE_URL
import com.example.okhttp.api.RetrofitService
import com.example.okhttp.repository.MovieRepository
import com.example.okhttp.repository.MovieRepositoryImp
import com.example.okhttp.repository.SavedMovieRepository
import com.example.okhttp.repository.SavedMovieRepositoryImp
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Provides
    fun provideFirebaseDatabase(): FirebaseDatabase = FirebaseDatabase.getInstance(FIREBASE_URL)
    @Provides
    fun provideService(): RetrofitService = RetrofitService()
    @Provides
    fun providesMovieRepository(imp: MovieRepositoryImp): MovieRepository = imp
    @Provides
    fun providesSavedMovieRepository(imp: SavedMovieRepositoryImp): SavedMovieRepository = imp

}
