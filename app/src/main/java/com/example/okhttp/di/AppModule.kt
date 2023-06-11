package com.example.okhttp.di

import FIREBASE_URL
import androidx.lifecycle.SavedStateHandle
import com.example.okhttp.api.RetrofitService
import com.example.okhttp.repository.MovieRepository
import com.example.okhttp.repository.MovieRepositoryImp
import com.example.okhttp.repository.SavedMovieRepository
import com.example.okhttp.repository.SavedMovieRepositoryImp
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Provides
    fun provideFirebaseDatabase(): FirebaseDatabase = FirebaseDatabase.getInstance(FIREBASE_URL)
    @Provides
    fun provideService(): RetrofitService = RetrofitService()
    @Provides
    fun providesSavedMovieRepository(imp: SavedMovieRepositoryImp): SavedMovieRepository = imp

    //@Provides
    //fun providesMovieRepository(imp: MovieRepositoryImp): MovieRepository = imp
    //пример без использования @Inject в provideMovieRepositoryImp
    @Provides
    fun provideMovieRepositoryImp(service: RetrofitService) = MovieRepositoryImp(service = service)

    @Module
    @InstallIn(ViewModelComponent::class)
    object ViewModelMovieModule {
        @Provides
        @ViewModelScoped //жизненный цикл ViewModelScope
        fun providesMovieRepository(imp: MovieRepositoryImp): MovieRepository = imp
    }
}
