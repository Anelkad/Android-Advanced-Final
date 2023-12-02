package com.example.okhttp.data.di

import API_KEY
import BASE_URL
import android.util.Log
import com.example.okhttp.data.BuildConfig
import com.example.okhttp.data.api.MovieApi
import com.example.okhttp.data.repository.MovieRepositoryImp
import com.example.okhttp.data.repository.SavedMovieRepositoryImp
import com.example.okhttp.domain.repository.MovieRepository
import com.example.okhttp.domain.repository.SavedMovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.HttpUrl
import okhttp3.Interceptor
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
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideOkHttpClient(
        @Named("KEY_INTERCEPTOR") apiKeyInterceptor: Interceptor,
        @Named("LOGGING_INTERCEPTOR") httpLoggingInterceptor: Interceptor
    ): OkHttpClient {
        val client = OkHttpClient.Builder()
            .addInterceptor(apiKeyInterceptor)
        if (BuildConfig.DEBUG) {
            client.addInterceptor(httpLoggingInterceptor)
        }
        return client.build()
    }

    @Provides
    @Singleton
    @Named("LOGGING_INTERCEPTOR")
    fun provideLoggingInterceptor(): Interceptor {
        return HttpLoggingInterceptor { message -> Log.d("OkHttp", message) }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    @Named("KEY_INTERCEPTOR")
    fun provideKeyInterceptor(): Interceptor {
        return Interceptor { chain: Interceptor.Chain ->
            val original = chain.request()
            val originalHttpUrl: HttpUrl = original.url
            val url = originalHttpUrl.newBuilder()
                .addQueryParameter("api_key", API_KEY)
                .build()
            val request = original.newBuilder()
                .url(url)
                .build()
            chain.proceed(request)
        }
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
    ): MovieRepository = MovieRepositoryImp(api = movieApi)

    @Provides
    @Singleton
    fun provideSavedMovieRepository(
        @Named("MOVIE_API") movieApi: MovieApi
    ): SavedMovieRepository = SavedMovieRepositoryImp(api = movieApi)
}
