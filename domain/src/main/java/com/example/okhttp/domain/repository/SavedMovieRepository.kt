package com.example.okhttp.domain.repository

import com.example.core.utils.CommonResult
import com.example.okhttp.domain.model.AddMovieResponse
import com.example.okhttp.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface SavedMovieRepository {
    fun getSavedMovieList(): Flow<CommonResult<List<Movie>>>
    suspend fun deleteMovie(movieId: Int): CommonResult<AddMovieResponse>
    suspend fun saveMovie(movieId: Int): CommonResult<AddMovieResponse>
    suspend fun rateMovie(movieId: Int, rating: Double): CommonResult<AddMovieResponse>
}