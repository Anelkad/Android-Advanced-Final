package com.example.okhttp.domain.repository

import com.example.core.utils.CommonResult
import com.example.okhttp.domain.model.Movie

interface SavedMovieRepository {
    suspend fun getSavedMovieList(): CommonResult<List<Movie>>
    suspend fun deleteMovie(movieId: Int): Int
    suspend fun saveMovie(movie: Movie): Movie
}