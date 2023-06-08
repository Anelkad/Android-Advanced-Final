package com.example.okhttp.api

import API_KEY
import LANGUAGE
import com.example.okhttp.models.MovieDetails
import com.example.okhttp.models.MovieListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {
    @GET("discover/movie?api_key=${API_KEY}&with_genres=16,18&language=${LANGUAGE}")
    suspend fun getMovieList(
        @Query("page")
        page: Int = 1
    ): MovieListResponse

    @GET("movie")
    suspend fun getMovie(
        @Path("movie_id")
        movie_id: String,
        @Query("api_key")
        api_key: String = API_KEY,
        @Query("language")
        language: String = LANGUAGE
    ): MovieDetails
}