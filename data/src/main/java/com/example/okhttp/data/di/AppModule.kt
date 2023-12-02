package com.example.okhttp.data.di

import BASE_URL
import FIREBASE_URL
import com.example.okhttp.data.api.MovieApi
import com.example.okhttp.data.repository.MovieRepositoryImp
import com.example.okhttp.data.repository.SavedMovieRepositoryImp
import com.example.okhttp.domain.repository.MovieRepository
import com.example.okhttp.domain.repository.SavedMovieRepository
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) //App lifecycle
object AppModule {
    @Provides
    @Singleton
    fun provideFirebaseDatabase(): FirebaseDatabase = FirebaseDatabase.getInstance(FIREBASE_URL)

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
        return client
    }

    @Provides
    @Singleton
    @Named("MOVIE_API")
    fun provideMovieApi(
        retrofit: Retrofit
    ): MovieApi = retrofit.create()

    @Provides
    @Singleton
    fun provideMovieRepository(
        @Named("MOVIE_API") movieApi: MovieApi,
    ): MovieRepository = MovieRepositoryImp(movieApi)

    @Provides
    @Singleton
    fun provideSavedMovieRepository(
        @Named("MOVIE_API") movieApi: MovieApi,
        firebaseDatabase: FirebaseDatabase
    ): SavedMovieRepository = SavedMovieRepositoryImp(api = movieApi, firebase = firebaseDatabase)
}
