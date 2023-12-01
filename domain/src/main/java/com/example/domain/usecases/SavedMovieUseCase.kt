package com.example.okhttp.savedMovieList

import com.example.domain.repository.SavedMovieRepository
import com.example.okhttp.utils.CommonResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SavedMovieUseCase @Inject constructor(
    private val savedMovieRepository: SavedMovieRepository,
) {
    fun getSavedMovieList(): Flow<CommonResult<ArrayList<com.example.domain.model.Movie>>> = savedMovieRepository.getSavedMovieList()

    suspend fun deleteMovie(movieId: Int): Int = savedMovieRepository.deleteMovie(movieId)

    suspend fun saveMovie(movie: com.example.domain.model.Movie): com.example.domain.model.Movie = savedMovieRepository.saveMovie(movie)

}
