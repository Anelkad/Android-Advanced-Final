package com.example.okhttp.domain.usecases

import com.example.core.utils.CommonResult
import com.example.okhttp.domain.model.Movie
import com.example.okhttp.domain.repository.SavedMovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SavedMovieUseCase @Inject constructor(
    private val savedMovieRepository: SavedMovieRepository,
) {
    fun getSavedMovieList(): Flow<CommonResult<ArrayList<Movie>>> = savedMovieRepository.getSavedMovieList()

    suspend fun deleteMovie(movieId: Int): Int = savedMovieRepository.deleteMovie(movieId)

    suspend fun saveMovie(movie: Movie): Movie = savedMovieRepository.saveMovie(movie)

}
