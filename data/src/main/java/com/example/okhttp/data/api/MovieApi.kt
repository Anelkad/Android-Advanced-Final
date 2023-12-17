package com.example.okhttp.data.api

import com.example.core.utils.ApiConstants.GENRE_DRAMA
import com.example.core.utils.ApiConstants.GENRE_FAMILY
import com.example.core.utils.ApiConstants.GENRE_FANTASY
import com.example.core.utils.ApiConstants.GENRE_ROMANCE
import com.example.core.utils.ApiConstants.KEYWORD_ANIME
import com.example.okhttp.data.modelDTO.AddFavoriteMovieRequest
import com.example.okhttp.data.modelDTO.AddMovieResponseDTO
import com.example.okhttp.data.modelDTO.MovieDetailsDTO
import com.example.okhttp.data.modelDTO.MovieIsSavedDTO
import com.example.okhttp.data.modelDTO.MovieListResponseDTO
import com.example.okhttp.data.modelDTO.UserDetailsResponse
import com.example.okhttp.domain.model.Rating
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
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
        withGenres: String = "$GENRE_DRAMA|$GENRE_ROMANCE|$GENRE_FAMILY|$GENRE_FANTASY",
        @Query("with_keywords")
        withKeywords: String = KEYWORD_ANIME
    ): MovieListResponseDTO

    @GET("movie/{movie_id}")
    suspend fun getMovie(
        @Path("movie_id")
        movieId: Int
    ): Response<MovieDetailsDTO>

    @GET("account/{user_id}/favorite/movies")
    suspend fun getFavoriteMovieList(
        @Path("user_id")
        userId: Int
    ): Response<MovieListResponseDTO>

    @POST("account/{user_id}/favorite")
    suspend fun addFavoriteMovie(
        @Path("user_id")
        userId: Int,
        @Body body: AddFavoriteMovieRequest
    ): Response<AddMovieResponseDTO>

    @GET("movie/{movie_id}/account_states")
    suspend fun getMovieIsSaved(
        @Path("movie_id")
        movieId: Int
    ): Response<MovieIsSavedDTO>

    @POST("movie/{movie_id}/rating")
    suspend fun addRatingMovie(
        @Path("movie_id")
        movieId: Int,
        @Body body: Rating
    ): Response<AddMovieResponseDTO>

    @DELETE("movie/{movie_id}/rating")
    suspend fun deleteRatingMovie(
        @Path("movie_id")
        movieId: Int
    ): Response<AddMovieResponseDTO>

    @GET("search/movie")
    suspend fun searchMovie(
        @Query("query")
        query: String
    ): Response<MovieListResponseDTO>
}