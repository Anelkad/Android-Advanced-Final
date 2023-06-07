package com.example.okhttp.repository

import com.example.okhttp.models.MovieItem
import com.example.okhttp.utils.Resource
import kotlinx.coroutines.flow.Flow

interface SavedMovieRepository {
    fun getSavedMovieList(): Flow<Resource<ArrayList<MovieItem>>>
    suspend fun deleteMovie(movieId: Int): Resource<Int>
    suspend fun saveMovie(movie: MovieItem): Resource<MovieItem>
}