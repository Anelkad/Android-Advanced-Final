package com.example.okhttp.repository

import com.example.okhttp.domain.Movie
import com.example.okhttp.utils.CommonResult
import kotlinx.coroutines.flow.Flow

interface SavedMovieRepository {
    fun getSavedMovieList(): Flow<CommonResult<ArrayList<Movie>>>
    suspend fun deleteMovie(movieId: Int): Int
    suspend fun saveMovie(movie: Movie): Movie
}