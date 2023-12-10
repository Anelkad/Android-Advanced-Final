package com.example.okhttp.data.di

import API_KEY
import AUTH_CLIENT
import BASE_URL
import ENCRYPTED_SHARED_PREFERENCES
import KEY_INTERCEPTOR
import LOGGING_INTERCEPTOR
import MOVIE_CLIENT
import SESSION_ID
import SESSION_INTERCEPTOR
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.okhttp.data.BuildConfig
import com.example.okhttp.data.api.MovieApi
import com.example.okhttp.data.local.SessionManager
import com.example.okhttp.data.local.SharedPreferencesFactory
import com.example.okhttp.data.modelDTO.RatingDTO
import com.example.okhttp.data.modelDTO.RatingDeserializer
import com.example.okhttp.data.repository.MovieRepositoryImp
import com.example.okhttp.data.repository.SavedMovieRepositoryImp
import com.example.okhttp.domain.repository.MovieRepository
import com.example.okhttp.domain.repository.SavedMovieRepository
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
@InstallIn(SingletonComponent::class)
object AppModule {

    private val gson = GsonBuilder()
        .registerTypeAdapter(RatingDTO::class.java, RatingDeserializer())
        .create()

    @Provides
    @Singleton
    @Named("MOVIE_RETROFIT")
    fun provideMovieRetrofit(
        @Named(MOVIE_CLIENT) okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides
    @Singleton
    @Named("AUTH_RETROFIT")
    fun provideAuthRetrofit(
        @Named(AUTH_CLIENT) okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides
    @Singleton
    @Named(ENCRYPTED_SHARED_PREFERENCES)
    fun provideSecureSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        SharedPreferencesFactory.getSharedPreferences(
            SharedPreferencesFactory.Type.ENCRYPTED,
            context
        )

    @Provides
    @Singleton
    fun provideSessionManager(
        @Named(ENCRYPTED_SHARED_PREFERENCES) sharedPreferences: SharedPreferences
    ): SessionManager = SessionManager(sharedPreferences)

    @Provides
    @Singleton
    @Named(MOVIE_CLIENT)
    fun provideMovieOkHttpClient(
        @Named(SESSION_INTERCEPTOR) apiKeyInterceptor: Interceptor,
        @Named(LOGGING_INTERCEPTOR) httpLoggingInterceptor: Interceptor
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
    @Named(AUTH_CLIENT)
    fun provideAuthOkHttpClient(
        @Named(KEY_INTERCEPTOR) apiKeyInterceptor: Interceptor,
        @Named(LOGGING_INTERCEPTOR) httpLoggingInterceptor: Interceptor
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
    @Named(LOGGING_INTERCEPTOR)
    fun provideLoggingInterceptor(): Interceptor {
        return HttpLoggingInterceptor { message -> Log.d("OkHttp", message) }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    @Named(KEY_INTERCEPTOR)
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
    @Named(SESSION_INTERCEPTOR)
    fun provideSessionInterceptor(): Interceptor {
        return Interceptor { chain: Interceptor.Chain ->
            val original = chain.request()
            val originalHttpUrl: HttpUrl = original.url
            val url = originalHttpUrl.newBuilder()
                .addQueryParameter("api_key", API_KEY)
                .addQueryParameter("session_id", SESSION_ID)
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
        @Named("MOVIE_RETROFIT") retrofit: Retrofit
    ): MovieApi = retrofit.create()

    @Provides
    @Singleton
    @Named("AUTH_API")
    fun provideAuthApi(
        @Named("AUTH_RETROFIT") retrofit: Retrofit
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
