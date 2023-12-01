package com.example.okhttp.repository

import com.example.domain.model.Movie
import com.example.okhttp.utils.CommonResult
import kotlinx.coroutines.flow.Flow

interface SavedMovieRepository {
    fun getSavedMovieList(): Flow<CommonResult<ArrayList<com.example.domain.model.Movie>>>
    suspend fun deleteMovie(movieId: Int): Int
    suspend fun saveMovie(movie: com.example.domain.model.Movie): com.example.domain.model.Movie
}