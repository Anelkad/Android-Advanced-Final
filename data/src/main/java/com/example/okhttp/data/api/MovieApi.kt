package com.example.okhttp.data.api

import API_KEY
import LANGUAGE
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
        @Query("api_key")
        apiKey: String = API_KEY,
        @Query("with_genres")
        withGenres: String = "16,18",
        @Query("language")
        language: String = LANGUAGE
    ): MovieListResponseDTO

    @GET("movie/{movie_id}")
    suspend fun getMovie(
        @Path("movie_id")
        movieId: Int,
        @Query("api_key")
        apiKey: String = API_KEY,
        @Query("language")
        language: String = LANGUAGE
    ): Response<MovieDetailsDTO>
}