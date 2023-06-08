package com.example.okhttp.repository

import com.example.okhttp.models.Movie
import com.example.okhttp.utils.Resource
import kotlinx.coroutines.flow.Flow

interface SavedMovieRepository {
    fun getSavedMovieList(): Flow<Resource<ArrayList<Movie>>>
    suspend fun deleteMovie(movieId: Int): Resource<Int>
    suspend fun saveMovie(movie: Movie): Resource<Movie>
}