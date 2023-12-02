package com.example.okhttp.data.api

import GENRE_ANIMATION
import GENRE_DRAMA
import LANGUAGE
import SESSION_ID
import com.example.okhttp.data.modelDTO.MovieDetailsDTO
import com.example.okhttp.data.modelDTO.MovieListResponseDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {
    @GET("discover/movie")
    suspend fun getMovieList(
        @Query("page")
        page: Int = 1,
        @Query("with_genres")
        withGenres: String = "$GENRE_ANIMATION,$GENRE_DRAMA",
        @Query("language")
        language: String = LANGUAGE
    ): MovieListResponseDTO

    @GET("movie/{movie_id}")
    suspend fun getMovie(
        @Path("movie_id")
        movieId: Int,
        @Query("language")
        language: String = LANGUAGE
    ): Response<MovieDetailsDTO>

    @GET("account/20775042/favorite/movies")
    suspend fun getFavoriteMovieList(
        @Query("session_id")
        sessionId: String = SESSION_ID,
    ): Response<MovieListResponseDTO>
}