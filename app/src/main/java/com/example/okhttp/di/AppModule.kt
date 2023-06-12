package com.example.okhttp.di

import FIREBASE_URL
import androidx.lifecycle.SavedStateHandle
import com.example.okhttp.api.RetrofitService
import com.example.okhttp.repository.MovieRepository
import com.example.okhttp.repository.MovieRepositoryImp
import com.example.okhttp.repository.SavedMovieRepository
import com.example.okhttp.repository.SavedMovieRepositoryImp
import com.google.firebase.database.FirebaseDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)//App lifecycle
object AppModule {
    @Provides
    @Singleton
    fun provideFirebaseDatabase(): FirebaseDatabase = FirebaseDatabase.getInstance(FIREBASE_URL)
    @Provides
    @Singleton
    fun provideService(): RetrofitService = RetrofitService()
    //@Provides
    //fun providesMovieRepository(imp: MovieRepositoryImp): MovieRepository = imp
    //пример без использования @Inject в provideMovieRepositoryImp
    @Provides
    @Singleton
    fun provideMovieRepositoryImp(service: RetrofitService) = MovieRepositoryImp(service = service)
    //todo providesMovieRepository => provideMovieRepositoryImp => provideService
}
