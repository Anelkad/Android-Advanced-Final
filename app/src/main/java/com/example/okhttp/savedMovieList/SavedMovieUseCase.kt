package com.example.okhttp.savedMovieList

import com.example.okhttp.models.Movie
import com.example.okhttp.repository.SavedMovieRepository
import com.example.okhttp.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SavedMovieUseCase @Inject constructor(
    private val savedMovieRepository: SavedMovieRepository,
) {
    fun getSavedMovieList(): Flow<Resource<ArrayList<Movie>>> = savedMovieRepository.getSavedMovieList()

    suspend fun deleteMovie(movieId: Int): Resource<Int> = savedMovieRepository.deleteMovie(movieId)

    suspend fun saveMovie(movie: Movie): Resource<Movie> = savedMovieRepository.saveMovie(movie)

}
