package com.example.okhttp.data.api

import GENRE_ANIMATION
import GENRE_DRAMA
import LANGUAGE
import com.example.okhttp.data.modelDTO.AddFavoriteMovieRequest
import com.example.okhttp.data.modelDTO.AddFavoriteMovieResponseDTO
import com.example.okhttp.data.modelDTO.MovieDetailsDTO
import com.example.okhttp.data.modelDTO.MovieIsSavedDTO
import com.example.okhttp.data.modelDTO.MovieListResponseDTO
import com.example.okhttp.data.modelDTO.UserDetailsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {
    @GET("account")
    suspend fun getUserId(): Response<UserDetailsResponse>

    @GET("discover/movie")
    suspend fun getMovieList(
        @Query("page")
        page: Int = 1,
        @Query("with_genres")
        withGenres: String = "$GENRE_ANIMATION,$GENRE_DRAMA",
        @Query("language")
        language: String? = LANGUAGE
    ): MovieListResponseDTO

    @GET("movie/{movie_id}")
    suspend fun getMovie(
        @Path("movie_id")
        movieId: Int,
        @Query("language")
        language: String? = LANGUAGE
    ): Response<MovieDetailsDTO>

    @GET("account/20775042/favorite/movies")
    suspend fun getFavoriteMovieList(
        @Query("language")
        language: String? = LANGUAGE
    ): Response<MovieListResponseDTO>
    //todo add shared prefs for current session
    @POST("account/20775042/favorite")
    suspend fun addFavoriteMovie(
        @Body body: AddFavoriteMovieRequest
    ): Response<AddFavoriteMovieResponseDTO>

    @GET("movie/{movie_id}/account_states")
    suspend fun getMovieIsSaved(
        @Path("movie_id")
        movieId: Int
    ): Response<MovieIsSavedDTO>
}