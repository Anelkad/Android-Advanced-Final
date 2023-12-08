package com.example.okhttp.domain.repository

import com.example.core.utils.CommonResult
import com.example.okhttp.domain.model.AddFavoriteMovieResponse
import com.example.okhttp.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface SavedMovieRepository {
    fun getSavedMovieList(): Flow<CommonResult<List<Movie>>>
    suspend fun deleteMovie(movieId: Int): CommonResult<AddFavoriteMovieResponse>
    suspend fun saveMovie(movieId: Int): CommonResult<AddFavoriteMovieResponse>
}