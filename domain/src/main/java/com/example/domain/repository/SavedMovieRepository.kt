package com.example.domain.repository

import com.example.core.utils.CommonResult
import com.example.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface SavedMovieRepository {
    fun getSavedMovieList(): Flow<CommonResult<ArrayList<Movie>>>
    suspend fun deleteMovie(movieId: Int): Int
    suspend fun saveMovie(movie: Movie): Movie
}