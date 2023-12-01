package com.example.okhttp.savedMovieList

import com.example.okhttp.domain.Movie
import com.example.okhttp.repository.SavedMovieRepository
import com.example.okhttp.utils.CommonResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SavedMovieUseCase @Inject constructor(
    private val savedMovieRepository: SavedMovieRepository,
) {
    fun getSavedMovieList(): Flow<CommonResult<ArrayList<Movie>>> = savedMovieRepository.getSavedMovieList()

    suspend fun deleteMovie(movieId: Int): Int = savedMovieRepository.deleteMovie(movieId)

    suspend fun saveMovie(movie: Movie): Movie = savedMovieRepository.saveMovie(movie)

}
